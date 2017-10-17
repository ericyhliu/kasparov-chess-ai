package kasparov;

/**
 * For making a move.
 *
 * @author Eric Liu
 */
public class MakeMove {

    static void hashPiece(BoardStructure boardStructure, int piece, int sqr) {
        boardStructure.positionKey ^= boardStructure.pieceKeys[piece][sqr];
    }

    static long hashCastle(BoardStructure boardStructure) {
        return boardStructure.positionKey ^ boardStructure.castleKeys[boardStructure.castlePerm];
    }

    static long hashSide(BoardStructure boardStructure) {
        return boardStructure.positionKey ^ boardStructure.sideKey;
    }

    static long hashEnPassant(BoardStructure boardStructure) {
        return boardStructure.positionKey ^ boardStructure.pieceKeys[BoardPiece.EMPTY.value][boardStructure.enPassant];
    }

    static int[] castlePerm = {
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 13, 15, 15, 15, 12, 15, 15, 14, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15,  7, 15, 15, 15,  3, 15, 15, 11, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15
    };

    static void clearPiece(BoardStructure boardStructure, int sqr) {
        int piece = boardStructure.pieces[sqr];
        int col = BoardConstants.pieceColor[piece];
        int tempPieceNum = -1;

        hashPiece(boardStructure, piece, sqr);

        boardStructure.pieces[sqr] = BoardPiece.EMPTY.value;
        boardStructure.material[col] -= BoardConstants.pieceValue[piece];

        if (BoardConstants.pieceBig[piece]) {
            boardStructure.pieceBig[col]--;

            if (BoardConstants.pieceMajor[piece]) {
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

    static void addPiece(BoardStructure boardStructure, int sqr, int piece) {
        int col = BoardConstants.pieceColor[piece];
        hashPiece(boardStructure, piece, sqr);

        boardStructure.pieces[sqr] = piece;

        if (BoardConstants.pieceBig[piece]) {
            boardStructure.pieceBig[col]++;
            if (BoardConstants.pieceMajor[piece]) {
                boardStructure.pieceMajor[col]++;
            } else {
                boardStructure.pieceMinor[col]++;
            }
        } else {
            boardStructure.setBit(boardStructure.pawns[col], boardStructure.sqr64(sqr));
            boardStructure.setBit(boardStructure.pawns[BoardColor.BOTH.value], boardStructure.sqr64(sqr));
        }

        boardStructure.material[col] += BoardConstants.pieceValue[piece];
        boardStructure.pieceList[piece][boardStructure.pieceNum[piece]++] = sqr;
    }

    static void movePiece(BoardStructure boardStructure, int from, int to) {
        int piece = boardStructure.pieces[from];
        int col = BoardConstants.pieceColor[piece];
        boolean tempPieceNum = false;

        hashPiece(boardStructure, piece, from);

        boardStructure.pieces[from] = BoardPiece.EMPTY.value;

        hashPiece(boardStructure, piece, to);

        boardStructure.pieces[to] = piece;

        if (!BoardConstants.pieceBig[piece]) {
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

    static boolean makeMove(BoardStructure boardStructure, int move) {
        int from = Move.from(move);
        int to = Move.to(move);
        int side = boardStructure.side;

        boardStructure.history[boardStructure.historyPly].posKey = boardStructure.positionKey;

        if ((move & Move.moveFlagEnPassant) != 0) {
            if (side == BoardColor.WHITE.value)
                clearPiece(boardStructure, to - 10);
            else
                clearPiece(boardStructure, to + 10);
        } else if ((move & Move.moveFlagCastle) != 0) {
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

        boardStructure.history[boardStructure.historyPly].move = move;
        boardStructure.history[boardStructure.historyPly].fiftyMove =
                boardStructure.fiftyMove;
        boardStructure.history[boardStructure.historyPly].enPassant =
                boardStructure.enPassant;
        boardStructure.history[boardStructure.historyPly].castlePerm =
                boardStructure.castlePerm;

        boardStructure.castlePerm &= castlePerm[from];
        boardStructure.castlePerm &= castlePerm[to];
        boardStructure.enPassant = BoardSquare.NONE.value;
        hashCastle(boardStructure);

        int captured = Move.captured(move);
        boardStructure.fiftyMove++;

        if (captured != BoardPiece.EMPTY.value) {
            clearPiece(boardStructure, to);
            boardStructure.fiftyMove = 0;
        }

        boardStructure.historyPly++;
        boardStructure.ply++;

        if (BoardConstants.piecePawn[boardStructure.pieces[from]]) {
            boardStructure.fiftyMove = 0;
            if ((move & Move.moveFlagPawnStart) != 0) {
                if (side == BoardColor.WHITE.value) {
                    boardStructure.enPassant = from + 10;
                } else {
                    boardStructure.enPassant = from - 10;
                }
                hashEnPassant(boardStructure);
            }
        }

        movePiece(boardStructure, from, to);

        int promotedPiece = Move.promoted(move);
        if (promotedPiece != BoardPiece.EMPTY.value) {
            clearPiece(boardStructure, to);
            addPiece(boardStructure, to, promotedPiece);
        }

        if (BoardConstants.pieceKing[boardStructure.pieces[to]])
            boardStructure.kingSqr[boardStructure.side] = to;

        boardStructure.side ^= 1;
        hashSide(boardStructure);

        if (SquareAttacked.squareAttacked(boardStructure.kingSqr[side],
                boardStructure.side, boardStructure)) {
            takeMove(boardStructure);
            return false;
        }

        return true;
    }

    static void takeMove(BoardStructure boardStructure) {
        boardStructure.historyPly--;
        boardStructure.ply--;

        int move = boardStructure.history[boardStructure.historyPly].move;
        int from = Move.from(move);
        int to = Move.to(move);

        if (boardStructure.enPassant != BoardSquare.NONE.value)
            hashEnPassant(boardStructure);
        hashCastle(boardStructure);

        boardStructure.castlePerm = boardStructure.history[boardStructure.historyPly].castlePerm;
        boardStructure.fiftyMove = boardStructure.history[boardStructure.historyPly].fiftyMove;
        boardStructure.enPassant = boardStructure.history[boardStructure.historyPly].enPassant;

        if (boardStructure.enPassant != BoardSquare.NONE.value)
            hashEnPassant(boardStructure);
        hashCastle(boardStructure);

        boardStructure.side ^= 1;
        hashSide(boardStructure);

        if ((move & Move.moveFlagEnPassant) != 0) {
            if (boardStructure.side == BoardColor.WHITE.value) {
                addPiece(boardStructure, to-10, BoardPiece.BLACK_PAWN.value);
            } else {
                addPiece(boardStructure, to+10, BoardPiece.WHITE_PAWN.value);
            }
        } else if ((move & Move.moveFlagCastle) != 0) {
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

        if (BoardConstants.pieceKing[boardStructure.pieces[from]]) {
            boardStructure.kingSqr[boardStructure.side] = from;
        }

        int captured = Move.captured(move);
        if (captured != BoardPiece.EMPTY.value) {
            addPiece(boardStructure, to, captured);
        }

        int promoted = Move.promoted(move);
        if (promoted != BoardPiece.EMPTY.value) {
            clearPiece(boardStructure, from);
            addPiece(boardStructure, from, (BoardConstants.pieceColor[promoted] == BoardColor.WHITE.value ?
                    BoardPiece.WHITE_PAWN.value : BoardPiece.BLACK_PAWN.value));
        }
    }

}
