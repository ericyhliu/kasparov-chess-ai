package kasparov;

/**
 * For making a move.
 *
 * @author Eric Liu
 */
public class MakeMove {

    /**
     * Hashes piece into the position key of the BoardStructure.
     *
     * @param boardStructure
     * @param piece
     * @param square
     * @throws IllegalArgumentException if the piece is invalid
     * @throws IllegalArgumentException if the square is invalid
     */
    protected static void hashPiece(BoardStructure boardStructure,
                                    int piece, int square) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("invalid piece");
        if (square < BoardSquare.A1.value ||
            square > BoardSquare.OFFBOARD.value)
            throw new IllegalArgumentException("invalid square");
        boardStructure.positionKey ^= boardStructure.pieceKeys[piece][square];
    }

    /**
     * Hashes castle permissions into the position key of the BoardStructure.
     *
     * @param boardStructure
     * @return hashed key with castle permissions
     */
    protected static long hashCastle(BoardStructure boardStructure) {
        return boardStructure.positionKey ^
               boardStructure.castleKeys[boardStructure.castlePerm];
    }

    /**
     * Hashes side into the position key of the BoardStructure.
     *
     * @param boardStructure
     * @return hashed key with side
     */
    protected static long hashSide(BoardStructure boardStructure) {
        return boardStructure.positionKey ^ boardStructure.sideKey;
    }

    /**
     * Hashes en passant square(s) into the position key of the BoardStructure.
     *
     * @param boardStructure
     * @return hashed key with en passant squares
     */
    protected static long hashEnPassant(BoardStructure boardStructure) {
        return boardStructure.positionKey ^
               boardStructure.pieceKeys[BoardPiece.EMPTY.value]
                       [boardStructure.enPassant];
    }

    /**
     * Removes a piece from the board.
     *
     * @param boardStructure
     * @param sqr
     */
    protected static void clearPiece(BoardStructure boardStructure, int sqr) {
        int piece = boardStructure.pieces[sqr];
        int col = BoardUtils.getPieceColor(piece);
        int tempPieceNum = -1;

        hashPiece(boardStructure, piece, sqr);

        boardStructure.pieces[sqr] = BoardPiece.EMPTY.value;
        boardStructure.material[col] -= BoardUtils.getPieceValue(piece);

        if (BoardUtils.isPieceBig(piece)) {
            boardStructure.pieceBig[col]--;

            if (BoardUtils.isPieceMajor(piece)) {
                boardStructure.pieceMajor[col]--;
            } else {
                boardStructure.pieceMinor[col]--;
            }
        } else {
            boardStructure.clearBit(boardStructure.pawns[col], boardStructure.sqr64(sqr));
            boardStructure.clearBit(boardStructure.pawns[BoardColor.BOTH.value], boardStructure.sqr64(sqr));
        }

        for (int i = 0; i < boardStructure.pieceNum[piece]; i++) {
            if (boardStructure.pieceList[piece][i] == sqr) {
                tempPieceNum = i;
                break;
            }
        }

        boardStructure.pieceNum[piece]--;
        boardStructure.pieceList[piece][tempPieceNum] = boardStructure.pieceList[piece][boardStructure.pieceNum[piece]];
    }

    /**
     * Adds a piece to the board.
     *
     * @param boardStructure
     * @param sqr
     * @param piece
     */
    protected static void addPiece(BoardStructure boardStructure, int sqr, int piece) {
        int col = BoardUtils.getPieceColor(piece);
        hashPiece(boardStructure, piece, sqr);

        boardStructure.pieces[sqr] = piece;

        if (BoardUtils.isPieceBig(piece)) {
            boardStructure.pieceBig[col]++;
            if (BoardUtils.isPieceMajor(piece)) {
                boardStructure.pieceMajor[col]++;
            } else {
                boardStructure.pieceMinor[col]++;
            }
        } else {
            boardStructure.setBit(boardStructure.pawns[col], boardStructure.sqr64(sqr));
            boardStructure.setBit(boardStructure.pawns[BoardColor.BOTH.value], boardStructure.sqr64(sqr));
        }

        boardStructure.material[col] += BoardUtils.getPieceValue(piece);
        boardStructure.pieceList[piece][boardStructure.pieceNum[piece]++] = sqr;
    }

    /**
     * Moves a piece on the board.
     *
     * @param boardStructure
     * @param from
     * @param to
     */
    protected static void movePiece(BoardStructure boardStructure, int from, int to) {
        int piece = boardStructure.pieces[from];
        int col = BoardUtils.getPieceColor(piece);
        boolean tempPieceNum = false;

        hashPiece(boardStructure, piece, from);

        boardStructure.pieces[from] = BoardPiece.EMPTY.value;

        hashPiece(boardStructure, piece, to);

        boardStructure.pieces[to] = piece;

        if (!BoardUtils.isPieceBig(piece)) {
            boardStructure.clearBit(boardStructure.pawns[col], boardStructure.sqr64(from));
            boardStructure.clearBit(boardStructure.pawns[BoardColor.BOTH.value], boardStructure.sqr64(from));
            boardStructure.setBit(boardStructure.pawns[col], boardStructure.sqr64(to));
            boardStructure.setBit(boardStructure.pawns[BoardColor.BOTH.value], boardStructure.sqr64(to));
        }

        for (int i = 0; i < boardStructure.pieceNum[piece]; i++) {
            if (boardStructure.pieceList[piece][i] == from) {
                boardStructure.pieceList[piece][i] = to;
                tempPieceNum = true;
                break;
            }
        }

        assert (tempPieceNum);
    }

    /**
     * Makes move.
     *
     * @param boardStructure
     * @param move
     * @return
     */
    protected static boolean makeMove(BoardStructure boardStructure, int move) {
        int from = MoveUtils.from(move);
        int to = MoveUtils.to(move);
        int side = boardStructure.side;

        boardStructure.history[boardStructure.historyPly].setPosKey(boardStructure.positionKey);

        if ((move & MoveUtils.MOVE_FLAG_EN_PASSANT) != 0) {
            if (side == BoardColor.WHITE.value)
                clearPiece(boardStructure, to - 10);
            else
                clearPiece(boardStructure, to + 10);
        } else if ((move & MoveUtils.MOVE_FLAG_CASTLE) != 0) {
            if (to == BoardSquare.C1.value) {
                movePiece(boardStructure, BoardSquare.A1.value,
                        BoardSquare.D1.value);
            } else if (to == BoardSquare.C8.value) {
                movePiece(boardStructure, BoardSquare.A8.value,
                        BoardSquare.D8.value);
            } else if (to == BoardSquare.G1.value) {
                movePiece(boardStructure, BoardSquare.H1.value,
                        BoardSquare.F1.value);
            } else if (to == BoardSquare.G8.value) {
                movePiece(boardStructure, BoardSquare.H8.value,
                        BoardSquare.F8.value);
            }
        }

        if (boardStructure.enPassant != BoardSquare.NONE.value)
            hashEnPassant(boardStructure);
        hashCastle(boardStructure);

        boardStructure.history[boardStructure.historyPly]
                .setMove(move);
        boardStructure.history[boardStructure.historyPly]
                .setFiftyMove(boardStructure.fiftyMove);
        boardStructure.history[boardStructure.historyPly]
                .setEnPassant(boardStructure.enPassant);
        boardStructure.history[boardStructure.historyPly]
                .setCastlePerm(boardStructure.castlePerm);
        boardStructure.castlePerm &= BoardUtils.getCastlePerm(from);
        boardStructure.castlePerm &= BoardUtils.getCastlePerm(to);
        boardStructure.enPassant = BoardSquare.NONE.value;
        hashCastle(boardStructure);

        int captured = MoveUtils.captured(move);
        boardStructure.fiftyMove++;

        if (captured != BoardPiece.EMPTY.value) {
            clearPiece(boardStructure, to);
            boardStructure.fiftyMove = 0;
        }

        boardStructure.historyPly++;
        boardStructure.ply++;

        if (BoardUtils.isPiecePawn(boardStructure.pieces[from])) {
            boardStructure.fiftyMove = 0;
            if ((move & MoveUtils.MOVE_FLAG_PAWN_START) != 0) {
                if (side == BoardColor.WHITE.value) {
                    boardStructure.enPassant = from + 10;
                } else {
                    boardStructure.enPassant = from - 10;
                }
                hashEnPassant(boardStructure);
            }
        }

        movePiece(boardStructure, from, to);

        int promotedPiece = MoveUtils.promoted(move);
        if (promotedPiece != BoardPiece.EMPTY.value) {
            clearPiece(boardStructure, to);
            addPiece(boardStructure, to, promotedPiece);
        }

        if (BoardUtils.isPieceKing(boardStructure.pieces[to]))
            boardStructure.kingSqr[boardStructure.side] = to;

        boardStructure.side ^= 1;
        hashSide(boardStructure);

        if (SquareAttacked.isSquareAttacked(boardStructure, boardStructure.kingSqr[side],
                boardStructure.side)) {
            takeMove(boardStructure);
            return false;
        }

        return true;
    }

    /**
     * Reverts previous move.
     *
     * @param boardStructure
     */
    protected static void takeMove(BoardStructure boardStructure) {
        boardStructure.historyPly--;
        boardStructure.ply--;

        int move = boardStructure.history[boardStructure.historyPly].getMove();
        int from = MoveUtils.from(move);
        int to = MoveUtils.to(move);

        if (boardStructure.enPassant != BoardSquare.NONE.value)
            hashEnPassant(boardStructure);
        hashCastle(boardStructure);

        boardStructure.castlePerm = boardStructure.history[boardStructure.historyPly].getCastlePerm();
        boardStructure.fiftyMove = boardStructure.history[boardStructure.historyPly].getFiftyMove();
        boardStructure.enPassant = boardStructure.history[boardStructure.historyPly].getEnPassant();

        if (boardStructure.enPassant != BoardSquare.NONE.value)
            hashEnPassant(boardStructure);
        hashCastle(boardStructure);

        boardStructure.side ^= 1;
        hashSide(boardStructure);

        if ((move & MoveUtils.MOVE_FLAG_EN_PASSANT) != 0) {
            if (boardStructure.side == BoardColor.WHITE.value) {
                addPiece(boardStructure, to-10, BoardPiece.BLACK_PAWN.value);
            } else {
                addPiece(boardStructure, to+10, BoardPiece.WHITE_PAWN.value);
            }
        } else if ((move & MoveUtils.MOVE_FLAG_CASTLE) != 0) {
            if (to == BoardSquare.C1.value) {
                movePiece(boardStructure, BoardSquare.D1.value, BoardSquare.A1.value);
            } else if (to == BoardSquare.C8.value) {
                movePiece(boardStructure, BoardSquare.D8.value, BoardSquare.A8.value);
            } else if (to == BoardSquare.G1.value) {
                movePiece(boardStructure, BoardSquare.F1.value, BoardSquare.H1.value);
            } else if (to == BoardSquare.G8.value) {
                movePiece(boardStructure, BoardSquare.F8.value, BoardSquare.H8.value);
            }
        }

        movePiece(boardStructure, to, from);

        if (BoardUtils.isPieceKing(boardStructure.pieces[from])) {
            boardStructure.kingSqr[boardStructure.side] = from;
        }

        int captured = MoveUtils.captured(move);
        if (captured != BoardPiece.EMPTY.value) {
            addPiece(boardStructure, to, captured);
        }

        int promoted = MoveUtils.promoted(move);
        if (promoted != BoardPiece.EMPTY.value) {
            clearPiece(boardStructure, from);
            addPiece(boardStructure, from, (BoardUtils.getPieceChar(promoted) == BoardColor.WHITE.value ?
                    BoardPiece.WHITE_PAWN.value : BoardPiece.BLACK_PAWN.value));
        }
    }

}
