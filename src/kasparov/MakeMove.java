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
    protected static void hashPiece(BoardStructure boardStructure, int piece, int square) {
        if (piece < BoardPiece.EMPTY.value || piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("invalid piece");
        if (square < BoardSquare.A1.value || square > BoardSquare.OFFBOARD.value)
            throw new IllegalArgumentException("invalid square");

        boardStructure.setPositionKey(boardStructure.getPositionKey() ^
        boardStructure.getPieceKey(piece, square));
    }

    /**
     * Hashes castle permissions into the position key of the BoardStructure.
     *
     * @param boardStructure
     * @return hashed key with castle permissions
     */
    protected static long hashCastle(BoardStructure boardStructure) {
        return boardStructure.getPositionKey() ^
               boardStructure.getCastleKey(boardStructure.getCastlePerm());
    }

    /**
     * Hashes side into the position key of the BoardStructure.
     *
     * @param boardStructure
     * @return hashed key with side
     */
    protected static long hashSide(BoardStructure boardStructure) {
        return boardStructure.getPositionKey() ^ boardStructure.getSideKey();
    }

    /**
     * Hashes en passant square(s) into the position key of the BoardStructure.
     *
     * @param boardStructure
     * @return hashed key with en passant squares
     */
    protected static long hashEnPassant(BoardStructure boardStructure) {
        return boardStructure.getPositionKey() ^
               boardStructure.getPieceKey(BoardPiece.EMPTY.value, boardStructure.getEnPassant());
    }

    /**
     * Removes a piece from the board.
     *
     * @param boardStructure
     * @param sqr
     */
    protected static void clearPiece(BoardStructure boardStructure, int sqr) {
        int piece = boardStructure.getPiece(sqr);
        int col = BoardUtils.getPieceColor(piece);
        int tempPieceNum = -1;

        hashPiece(boardStructure, piece, sqr);


        boardStructure.setPiece(BoardPiece.EMPTY.value, sqr);
        boardStructure.setMaterial(boardStructure.getMaterial(col) -
                BoardUtils.getPieceValue(piece), col);

        if (BoardUtils.isPieceBig(piece)) {
            boardStructure.setPieceNumBig(boardStructure.getPieceNumBig(col) - 1, col);

            if (BoardUtils.isPieceMajor(piece))
                boardStructure.setPieceNumMajor(boardStructure.getPieceNumMajor(col) - 1, col);
            else
                boardStructure.setPieceNumMinor(boardStructure.getPieceNumMinor(col) - 1, col);
        } else {
            boardStructure.clearBit(boardStructure.getPawn(col), boardStructure.sqr64(sqr));
            boardStructure.clearBit(boardStructure.getPawn(BoardColor.BOTH.value),
                    boardStructure.sqr64(sqr));
        }

        for (int i = 0; i < boardStructure.getPieceNum(piece); i++) {
            if (boardStructure.getPieceListEntry(piece, i) == sqr) {
                tempPieceNum = i;
                break;
            }
        }

        boardStructure.setPieceNum(boardStructure.getPieceNum(piece) - 1, piece);
        boardStructure.setPieceListEntry(boardStructure.getPieceListEntry(piece,
                boardStructure.getPieceNum(piece)), piece, tempPieceNum);
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

        boardStructure.setPiece(piece, sqr);

        if (BoardUtils.isPieceBig(piece)) {
            boardStructure.setPieceNumBig(boardStructure.getPieceNumBig(col) + 1, col);

            if (BoardUtils.isPieceMajor(piece))
                boardStructure.setPieceNumMajor(boardStructure.getPieceNumMajor(col) + 1, col);
            else
                boardStructure.setPieceNumMinor(boardStructure.getPieceNumMinor(col) + 1, col);
        } else {
            boardStructure.setBit(boardStructure.getPawn(col), boardStructure.sqr64(sqr));
            boardStructure.setBit(boardStructure.getPawn(BoardColor.BOTH.value),
                    boardStructure.sqr64(sqr));
        }

        boardStructure.setMaterial(boardStructure.getMaterial(col) +
                BoardUtils.getPieceValue(piece), col);
        boardStructure.setPieceListEntry(sqr, piece, boardStructure.getPieceNum(piece));
        boardStructure.setPieceNum(boardStructure.getPieceNum(piece) + 1, piece);
    }

    /**
     * Moves a piece on the board.
     *
     * @param boardStructure
     * @param from
     * @param to
     */
    protected static void movePiece(BoardStructure boardStructure,
                                    int from, int to) {
        int piece = boardStructure.getPiece(from);
        int col = BoardUtils.getPieceColor(piece);
        boolean tempPieceNum = false;

        hashPiece(boardStructure, piece, from);
        boardStructure.setPiece(BoardPiece.EMPTY.value, from);
        hashPiece(boardStructure, piece, to);
        boardStructure.setPiece(piece, to);

        if (!BoardUtils.isPieceBig(piece)) {
            boardStructure.clearBit(boardStructure.getPawn(col), boardStructure.sqr64(from));
            boardStructure.clearBit(boardStructure.getPawn(BoardColor.BOTH.value),
                    boardStructure.sqr64(from));
            boardStructure.setBit(boardStructure.getPawn(col), boardStructure.sqr64(to));
            boardStructure.setBit(boardStructure.getPawn(BoardColor.BOTH.value),
                    boardStructure.sqr64(to));
        }

        for (int i = 0; i < boardStructure.getPieceNum(piece); i++) {
            if (boardStructure.getPieceListEntry(piece, i) == from) {
                boardStructure.setPieceListEntry(to, piece, i);
                tempPieceNum = true;
                break;
            }
        }
    }

    /**
     * Makes move.
     *
     * @param boardStructure
     * @param move
     * @return
     */
    protected static boolean makeMove(BoardStructure boardStructure,
                                      int move) {
        int from = MoveUtils.from(move);
        int to = MoveUtils.to(move);
        int side = boardStructure.getSide();

        boardStructure.getHistoryEntry(boardStructure.getHistoryPly())
                .setPosKey(boardStructure.getPositionKey());

        if ((move & MoveUtils.MOVE_FLAG_EN_PASSANT) != 0) {
            if (side == BoardColor.WHITE.value)
                clearPiece(boardStructure, to - 10);
            else
                clearPiece(boardStructure, to + 10);
        } else if ((move & MoveUtils.MOVE_FLAG_CASTLE) != 0) {
            if (to == BoardSquare.C1.value)
                movePiece(boardStructure, BoardSquare.A1.value, BoardSquare.D1.value);
            else if (to == BoardSquare.C8.value)
                movePiece(boardStructure, BoardSquare.A8.value, BoardSquare.D8.value);
            else if (to == BoardSquare.G1.value)
                movePiece(boardStructure, BoardSquare.H1.value, BoardSquare.F1.value);
            else if (to == BoardSquare.G8.value)
                movePiece(boardStructure, BoardSquare.H8.value, BoardSquare.F8.value);
        }

        if (boardStructure.getEnPassant() != BoardSquare.NONE.value)
            hashEnPassant(boardStructure);

        hashCastle(boardStructure);

        boardStructure.getHistoryEntry(boardStructure.getHistoryPly())
                .setMove(move);
        boardStructure.getHistoryEntry(boardStructure.getHistoryPly())
                .setFiftyMove(boardStructure.getFiftyMove());
        boardStructure.getHistoryEntry(boardStructure.getHistoryPly())
                .setEnPassant(boardStructure.getEnPassant());
        boardStructure.getHistoryEntry(boardStructure.getHistoryPly())
                .setCastlePerm(boardStructure.getCastlePerm());
        boardStructure.setCastlePerm(boardStructure.getCastlePerm() &
                BoardUtils.getCastlePerm(from));
        boardStructure.setCastlePerm(boardStructure.getCastlePerm() &
                BoardUtils.getCastlePerm(to));
        boardStructure.setEnPassant(BoardSquare.NONE.value);
        hashCastle(boardStructure);

        int captured = MoveUtils.captured(move);

        boardStructure.setFiftyMove(boardStructure.getFiftyMove() + 1);

        if (captured != BoardPiece.EMPTY.value) {
            clearPiece(boardStructure, to);
            boardStructure.setFiftyMove(0);
        }

        boardStructure.setHistoryPly(boardStructure.getHistoryPly() + 1);
        boardStructure.setPly(boardStructure.getPly() + 1);

        if (BoardUtils.isPiecePawn(boardStructure.getPiece(from))) {
            boardStructure.setFiftyMove(0);

            if ((move & MoveUtils.MOVE_FLAG_PAWN_START) != 0) {
                if (side == BoardColor.WHITE.value)
                    boardStructure.setEnPassant(from + 10);
                else
                    boardStructure.setEnPassant(from - 10);

                hashEnPassant(boardStructure);
            }
        }

        movePiece(boardStructure, from, to);

        int promotedPiece = MoveUtils.promoted(move);
        if (promotedPiece != BoardPiece.EMPTY.value) {
            clearPiece(boardStructure, to);
            addPiece(boardStructure, to, promotedPiece);
        }

        if (BoardUtils.isPieceKing(boardStructure.getPiece(to)))
            boardStructure.setKing(to, boardStructure.getSide());

        boardStructure.setSide(boardStructure.getSide() ^ 1);
        hashSide(boardStructure);

        if (SquareAttacked.isSquareAttacked(boardStructure, boardStructure.getKing(side),
                boardStructure.getSide())) {
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
        boardStructure.setHistoryPly(boardStructure.getHistoryPly() - 1);
        boardStructure.setPly(boardStructure.getPly() - 1);

        int move = boardStructure.getHistoryEntry(boardStructure.getHistoryPly()).getMove();
        int from = MoveUtils.from(move);
        int to = MoveUtils.to(move);

        if (boardStructure.getEnPassant() != BoardSquare.NONE.value)
            hashEnPassant(boardStructure);

        hashCastle(boardStructure);

        boardStructure.setCastlePerm(boardStructure
                .getHistoryEntry(boardStructure.getHistoryPly())
                .getCastlePerm());

        boardStructure.setFiftyMove(boardStructure
                .getHistoryEntry(boardStructure.getHistoryPly())
                .getFiftyMove());

        boardStructure.setEnPassant(boardStructure
                .getHistoryEntry(boardStructure.getHistoryPly())
                .getEnPassant());

        if (boardStructure.getEnPassant() != BoardSquare.NONE.value)
            hashEnPassant(boardStructure);

        hashCastle(boardStructure);

        boardStructure.setSide(boardStructure.getSide() ^ 1);
        hashSide(boardStructure);

        if ((move & MoveUtils.MOVE_FLAG_EN_PASSANT) != 0) {
            if (boardStructure.getSide() == BoardColor.WHITE.value)
                addPiece(boardStructure, to-10, BoardPiece.BLACK_PAWN.value);
            else
                addPiece(boardStructure, to+10, BoardPiece.WHITE_PAWN.value);
        } else if ((move & MoveUtils.MOVE_FLAG_CASTLE) != 0) {
            if (to == BoardSquare.C1.value)
                movePiece(boardStructure, BoardSquare.D1.value, BoardSquare.A1.value);
            else if (to == BoardSquare.C8.value)
                movePiece(boardStructure, BoardSquare.D8.value, BoardSquare.A8.value);
            else if (to == BoardSquare.G1.value)
                movePiece(boardStructure, BoardSquare.F1.value, BoardSquare.H1.value);
            else if (to == BoardSquare.G8.value)
                movePiece(boardStructure, BoardSquare.F8.value, BoardSquare.H8.value);
        }

        movePiece(boardStructure, to, from);

        if (BoardUtils.isPieceKing(boardStructure.getPiece(from)))
            boardStructure.setKing(from, boardStructure.getSide());

        int captured = MoveUtils.captured(move);
        if (captured != BoardPiece.EMPTY.value)
            addPiece(boardStructure, to, captured);

        int promoted = MoveUtils.promoted(move);
        if (promoted != BoardPiece.EMPTY.value) {
            clearPiece(boardStructure, from);
            addPiece(boardStructure, from,
                (BoardUtils.getPieceColor(promoted) == BoardColor.WHITE.value ?
                BoardPiece.WHITE_PAWN.value :
                BoardPiece.BLACK_PAWN.value));
        }
    }

}
