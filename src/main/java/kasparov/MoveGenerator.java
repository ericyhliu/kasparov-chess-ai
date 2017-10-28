package kasparov;

/**
 * Generates moves.
 *
 * @author Eric Liu
 */
class MoveGenerator {

    /**
     * Array of slider pieces.
     */
    protected static final int[] loopPieceSlides = {
        BoardPiece.WHITE_BISHOP.value,
        BoardPiece.WHITE_ROOK.value,
        BoardPiece.WHITE_QUEEN.value,
        0,
        BoardPiece.BLACK_BISHOP.value,
        BoardPiece.BLACK_ROOK.value,
        BoardPiece.BLACK_QUEEN.value,
        0
    };

    /**
     * Array of non slider pieces.
     */
    protected static final int[] loopPieceNonSlides = {
        BoardPiece.WHITE_KNIGHT.value,
        BoardPiece.WHITE_KING.value,
        0,
        BoardPiece.BLACK_KNIGHT.value,
        BoardPiece.BLACK_KING.value,
        0
    };

    /**
     * Slider piece array indices.
     */
    protected static final int[] loopSlideIndex = {
        0, 4
    };

    /**
     * Non-slider piece array indices.
     */
    protected static final int[] loopNonSlideIndex = {
        0, 3
    };

    /**
     * Piece directions.
     */
    protected static final int[][] pieceDirections = {
        { 0,   0,   0,   0,   0,   0,   0},
        { 0,   0,   0,   0,   0,   0,   0},
        {-8, -19, -21, -12,   8,  19,   21,  12},
        {-9, -11,  11,   9,   0,   0,    0,   0},
        {-1, -10,   1,  10,   0,   0,    0,   0},
        {-1, -10,   1,  10,  -9, -11,   11,   9},
        {-1, -10,   1,  10,  -9, -11,   11,   9},
        { 0,   0,   0,   0,   0,   0,   0},
        {-8, -19, -21, -12,   8,  19,   21,  12},
        {-9, -11,  11,   9,   0,   0,    0,   0},
        {-1, -10,   1,  10,   0,   0,    0,   0},
        {-1, -10,   1,  10,  -9, -11,   11,   9},
        {-1, -10,   1,  10,  -9, -11,   11,   9}
    };

    /**
     * Direction numbers.
     */
    protected static final int[] numDirections = {
        0, 0, 8, 4, 4, 8, 8, 0, 8, 4, 4, 8, 8
    };

    /**
     * Victim scores.
     */
    protected static final int[] victimScore = {
        0, // Empty piece
        100, 200, 300, 400, 500, 600, // White pieces
        100, 200, 300, 400, 500, 600  // Black pieces
    };

    /**
     * Most valuable victim, least valuable attacker matrix.
     */
    protected static int[][] mvvLva = new int[13][13];


    /**
     * Initialize the most valuable victim, least valuable attacker matrix.
     */
    protected static void initMvvLva() {
        for (int attacker = BoardPiece.WHITE_PAWN.value;
             attacker <= BoardPiece.BLACK_KING.value; attacker++) {
            for (int victim = BoardPiece.WHITE_PAWN.value; victim <=
                    BoardPiece.BLACK_KING.value; victim++) {
                mvvLva[victim][attacker] = victimScore[victim] + 6 -
                        (victimScore[attacker]/100);
            }
        }
    }

    /**
     * Check that a move exists.
     *
     * @param boardStructure
     * @param move
     * @return true if the move exists, false otherwise
     */
    protected static boolean moveExists(BoardStructure boardStructure,
                                        int move) {
        MoveList moveList = new MoveList();
        MoveGenerator.generateAllMoves(boardStructure, moveList);
        for (int moveNum = 0; moveNum < moveList.getCount(); moveNum++) {
            if (!MakeMove.makeMove(boardStructure, moveList.getMove(moveNum).getMove()))
                continue;
            MakeMove.takeMove(boardStructure);
            if (moveList.getMove(moveNum).getMove() == move)
                return true;
        }
        return false;
    }

    /**
     * Create move.
     *
     * @param from
     * @param to
     * @param captured
     * @param promoted
     * @param flag
     * @return move
     */
    protected static int move(int from, int to, int captured, int promoted, int flag) {
        return from | ((to << 7) | (captured << 14)) | (promoted << 20) | flag;
    }

    /**
     * Add a quiet move to the BoardStructure.
     *
     * @param boardStructure
     * @param move
     * @param moveList
     */
    protected static void addQuietMove(BoardStructure boardStructure,
                                       int move, MoveList moveList) {

        moveList.setMove(new Move(), moveList.getCount());
        moveList.getMove(moveList.getCount()).setMove(move);

        if (boardStructure.getSearchKillersEntry(0, boardStructure.getPly()) == move)
            moveList.getMove(moveList.getCount()).setScore(900000);
        else if (boardStructure.getSearchKillersEntry(1, boardStructure.getPly()) == move)
            moveList.getMove(moveList.getCount()).setScore(800000);
        else {
            moveList.getMove(moveList.getCount()).setScore(boardStructure
                    .getSearchHistoryEntry(boardStructure
                    .getPiece(MoveUtils.from(move)), MoveUtils.to(move)));
        }
        moveList.setCount(moveList.getCount() + 1);
    }

    /**
     * Add a capture move to the BoardStructure.
     *
     * @param boardStructure
     * @param move
     * @param moveList
     */
    protected static void addCaptureMove(BoardStructure boardStructure, int move,
                                         MoveList moveList) {
        moveList.setMove(new Move(), moveList.getCount());
        moveList.getMove(moveList.getCount()).setMove(move);
        moveList.getMove(moveList.getCount()).setScore(mvvLva[MoveUtils.captured(move)]
                [boardStructure.getPiece(MoveUtils.from(move))] + 1000000);
        moveList.setCount(moveList.getCount() + 1);
    }

    /**
     * Add an en passant move to the BoardStructure.
     *
     * @param boardStructure
     * @param move
     * @param moveList
     */
    protected static void addEnPassantMove(BoardStructure boardStructure, int move,
                                           MoveList moveList) {
        moveList.setMove(new Move(), moveList.getCount());
        moveList.getMove(moveList.getCount()).setMove(move);
        moveList.getMove(moveList.getCount()).setScore(105 + 1000000);
        moveList.setCount(moveList.getCount() + 1);
    }

    /**
     * Add a white pawn capture move.
     *
     * @param boardStructure
     * @param from
     * @param to
     * @param capture
     * @param moveList
     */
    protected static void addWhitePawnCaptureMove(BoardStructure boardStructure,
                                                  int from, int to, int capture,
                                                  MoveList moveList) {
        if (boardStructure.getRankBoardEntry(from) == BoardRank.RANK_7.value) {
            addCaptureMove(boardStructure, move(from, to, capture,
                    BoardPiece.WHITE_QUEEN.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture,
                    BoardPiece.WHITE_ROOK.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture,
                    BoardPiece.WHITE_BISHOP.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture,
                    BoardPiece.WHITE_KNIGHT.value, 0), moveList);
        } else {
            addCaptureMove(boardStructure, move(from, to, capture,
                    BoardPiece.EMPTY.value, 0), moveList);
        }
    }

    /**
     * Add a white pawn non-capture move.
     *
     * @param boardStructure
     * @param from
     * @param to
     * @param moveList
     */
    protected static void addWhitePawnMove(BoardStructure boardStructure,
                                           int from, int to,
                                           MoveList moveList) {
        if (boardStructure.getRankBoardEntry(from) == BoardRank.RANK_7.value) {
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value,
                    BoardPiece.WHITE_QUEEN.value, 0), moveList);
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value,
                    BoardPiece.WHITE_ROOK.value, 0), moveList);
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value,
                    BoardPiece.WHITE_BISHOP.value, 0), moveList);
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value,
                    BoardPiece.WHITE_KNIGHT.value, 0), moveList);
        } else {
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value,
                    BoardPiece.EMPTY.value, 0), moveList);
        }
    }

    /**
     * Add a black pawn capture move.
     *
     * @param boardStructure
     * @param from
     * @param to
     * @param capture
     * @param moveList
     */
    protected static void addBlackPawnCaptureMove(BoardStructure boardStructure,
                                                  int from, int to, int capture,
                                                  MoveList moveList) {
        if (boardStructure.getRankBoardEntry(from) == BoardRank.RANK_2.value) {
            addCaptureMove(boardStructure, move(from, to, capture,
                    BoardPiece.BLACK_QUEEN.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture,
                    BoardPiece.BLACK_ROOK.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture,
                    BoardPiece.BLACK_BISHOP.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture,
                    BoardPiece.BLACK_KNIGHT.value, 0), moveList);
        } else {
            addCaptureMove(boardStructure, move(from, to, capture,
                    BoardPiece.EMPTY.value, 0), moveList);
        }
    }

    /**
     * Add a black pawn non-capture move.
     *
     * @param boardStructure
     * @param from
     * @param to
     * @param moveList
     */
    protected static void addBlackPawnMove(BoardStructure boardStructure,
                                           int from, int to,
                                           MoveList moveList) {
        if (boardStructure.getRankBoardEntry(from) == BoardRank.RANK_2.value) {
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value,
                    BoardPiece.BLACK_QUEEN.value, 0), moveList);
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value,
                    BoardPiece.BLACK_ROOK.value, 0), moveList);
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value,
                    BoardPiece.BLACK_BISHOP.value, 0), moveList);
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value,
                    BoardPiece.BLACK_KNIGHT.value, 0), moveList);
        } else {
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value,
                    BoardPiece.EMPTY.value, 0), moveList);
        }
    }

    /**
     * Generate all valid moves for the BoardStructure.
     * @param boardStructure
     * @param moveList
     */
    protected static void generateAllMoves(BoardStructure boardStructure,
                                           MoveList moveList) {
        moveList.setCount(0);

        int piece = BoardPiece.EMPTY.value;
        int side = boardStructure.getSide();
        int sqr = 0;
        int tempSqr = 0;
        int pieceNum = 0;
        int dir = 0;
        int index = 0;
        int pieceIndex = 0;

        if (side == BoardColor.WHITE.value) {
            for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(BoardPiece.WHITE_PAWN.value);
                 ++pieceNum) {
                sqr = boardStructure.getPieceListEntry(BoardPiece.WHITE_PAWN.value, pieceNum);

                if (boardStructure.getPiece(sqr + 10) == BoardPiece.EMPTY.value) {
                    addWhitePawnMove(boardStructure, sqr, sqr + 10, moveList);

                    if (boardStructure.getRankBoardEntry(sqr) == BoardRank.RANK_2.value &&
                            boardStructure.getPiece(sqr + 20) == BoardPiece.EMPTY.value) {
                        addQuietMove(boardStructure, move(sqr, sqr + 20, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, MoveUtils.MOVE_FLAG_PAWN_START), moveList);
                    }
                }

                if (!BoardUtils.isSquareOffBoard(boardStructure, sqr + 9) &&
                    BoardUtils.getPieceColor(boardStructure.getPiece(sqr + 9)) ==
                            BoardColor.BLACK.value) {
                    addWhitePawnCaptureMove(boardStructure, sqr, sqr + 9,
                            boardStructure.getPiece(sqr + 9), moveList);
                }

                if (!BoardUtils.isSquareOffBoard(boardStructure, sqr + 11) &&
                    BoardUtils.getPieceColor(boardStructure.getPiece(sqr + 11)) ==
                            BoardColor.BLACK.value) {
                    addWhitePawnCaptureMove(boardStructure, sqr, sqr + 11,
                            boardStructure.getPiece(sqr + 11), moveList);
                }

                if (boardStructure.getEnPassant() != BoardSquare.NONE.value) {
                    if (sqr + 9 == boardStructure.getEnPassant()) {
                        addEnPassantMove(boardStructure, move(sqr, sqr + 9,
                                BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, MoveUtils.MOVE_FLAG_EN_PASSANT),
                                moveList);
                    }

                    if (sqr + 11 == boardStructure.getEnPassant()) {
                        addEnPassantMove(boardStructure, move(sqr, sqr + 11,
                                BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, MoveUtils.MOVE_FLAG_EN_PASSANT),
                                moveList);
                    }
                }
            }

            if ((boardStructure.getCastlePerm() & BoardCastle.WHITE_KING_CASTLE.value) != 0) {
                if (boardStructure.getPiece(BoardSquare.F1.value) == BoardPiece.EMPTY.value &&
                    boardStructure.getPiece(BoardSquare.G1.value) == BoardPiece.EMPTY.value) {
                    if (!SquareAttacked.isSquareAttacked(boardStructure, BoardSquare.E1.value,
                            BoardColor.BLACK.value) &&
                        !SquareAttacked.isSquareAttacked(boardStructure, BoardSquare.F1.value,
                                BoardColor.BLACK.value)) {
                        addQuietMove(boardStructure, move(BoardSquare.E1.value,
                                BoardSquare.G1.value,
                                BoardPiece.EMPTY.value, BoardPiece.EMPTY.value,
                                MoveUtils.MOVE_FLAG_CASTLE), moveList);
                    }
                }
            }

            if ((boardStructure.getCastlePerm() & BoardCastle.WHITE_QUEEN_CASTLE.value) != 0) {
                if (boardStructure.getPiece(BoardSquare.D1.value) == BoardPiece.EMPTY.value &&
                    boardStructure.getPiece(BoardSquare.C1.value) == BoardPiece.EMPTY.value &&
                    boardStructure.getPiece(BoardSquare.B1.value) == BoardPiece.EMPTY.value) {
                    if (!SquareAttacked.isSquareAttacked(boardStructure, BoardSquare.E1.value,
                            BoardColor.BLACK.value) &&
                        !SquareAttacked.isSquareAttacked(boardStructure, BoardSquare.D1.value,
                                BoardColor.BLACK.value)) {
                        addQuietMove(boardStructure, move(BoardSquare.E1.value,
                                BoardSquare.C1.value,
                                BoardPiece.EMPTY.value, BoardPiece.EMPTY.value,
                                MoveUtils.MOVE_FLAG_CASTLE), moveList);
                    }
                }
            }

        } else {
            for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(BoardPiece.BLACK_PAWN.value);
                 ++pieceNum) {
                sqr = boardStructure.getPieceListEntry(BoardPiece.BLACK_PAWN.value, pieceNum);
                assert(BoardUtils.isSquareOnBoard(boardStructure, sqr));

                if (boardStructure.getPiece(sqr - 10) == BoardPiece.EMPTY.value) {
                    addBlackPawnMove(boardStructure, sqr, sqr - 10, moveList);

                    if (boardStructure.getRankBoardEntry(sqr) == BoardRank.RANK_7.value &&
                            boardStructure.getPiece(sqr - 20) == BoardPiece.EMPTY.value) {
                        addQuietMove(boardStructure, move(sqr, sqr - 20, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, MoveUtils.MOVE_FLAG_PAWN_START), moveList);
                    }
                }

                if (!BoardUtils.isSquareOffBoard(boardStructure, sqr - 9) &&
                        BoardUtils.getPieceColor(boardStructure.getPiece(sqr - 9)) ==
                                BoardColor.WHITE.value) {
                    addBlackPawnCaptureMove(boardStructure, sqr, sqr - 9,
                            boardStructure.getPiece(sqr - 9), moveList);
                }

                if (!BoardUtils.isSquareOffBoard(boardStructure, sqr - 11) &&
                        BoardUtils.getPieceColor(boardStructure.getPiece(sqr - 11)) ==
                                BoardColor.WHITE.value) {
                    addBlackPawnCaptureMove(boardStructure, sqr, sqr - 11,
                            boardStructure.getPiece(sqr - 11), moveList);
                }

                if (boardStructure.getEnPassant() != BoardSquare.NONE.value) {
                    if (sqr - 9 == boardStructure.getEnPassant()) {
                        addEnPassantMove(boardStructure, move(sqr, sqr - 9, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, MoveUtils.MOVE_FLAG_EN_PASSANT), moveList);
                    }

                    if (sqr - 11 == boardStructure.getEnPassant()) {
                        addEnPassantMove(boardStructure, move(sqr, sqr - 11, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, MoveUtils.MOVE_FLAG_EN_PASSANT), moveList);
                    }
                }
            }

            if ((boardStructure.getCastlePerm() & BoardCastle.BLACK_KING_CASTLE.value) != 0) {
                if (boardStructure.getPiece(BoardSquare.F8.value) == BoardPiece.EMPTY.value &&
                    boardStructure.getPiece(BoardSquare.G8.value) == BoardPiece.EMPTY.value) {
                    if (!SquareAttacked.isSquareAttacked(boardStructure, BoardSquare.E8.value,
                            BoardColor.WHITE.value) &&
                        !SquareAttacked.isSquareAttacked(boardStructure, BoardSquare.F8.value,
                                BoardColor.WHITE.value)) {
                        addQuietMove(boardStructure, move(BoardSquare.E8.value,
                                BoardSquare.G8.value,
                                BoardPiece.EMPTY.value, BoardPiece.EMPTY.value,
                                MoveUtils.MOVE_FLAG_CASTLE), moveList);
                    }
                }
            }

            if ((boardStructure.getCastlePerm() & BoardCastle.BLACK_QUEEN_CASTLE.value) != 0) {
                if (boardStructure.getPiece(BoardSquare.D8.value) == BoardPiece.EMPTY.value &&
                    boardStructure.getPiece(BoardSquare.C8.value) == BoardPiece.EMPTY.value &&
                    boardStructure.getPiece(BoardSquare.B8.value) == BoardPiece.EMPTY.value) {
                    if (!SquareAttacked.isSquareAttacked(boardStructure, BoardSquare.E8.value,
                            BoardColor.WHITE.value) &&
                        !SquareAttacked.isSquareAttacked(boardStructure, BoardSquare.D8.value,
                                BoardColor.WHITE.value)) {
                        addQuietMove(boardStructure, move(BoardSquare.E8.value,
                                BoardSquare.C8.value,
                                BoardPiece.EMPTY.value, BoardPiece.EMPTY.value,
                                MoveUtils.MOVE_FLAG_CASTLE), moveList);
                    }
                }
            }

        }

        // Slide pieces:
        pieceIndex = loopSlideIndex[side];
        piece = loopPieceSlides[pieceIndex++];
        while (piece != 0) {

            for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(piece); pieceNum++) {
                sqr = boardStructure.getPieceListEntry(piece, pieceNum);

                for (int i = 0; i < numDirections[piece]; i++) {
                    dir = pieceDirections[piece][i];
                    tempSqr = sqr + dir;

                    while (!BoardUtils.isSquareOffBoard(boardStructure, tempSqr)) {
                        if (boardStructure.getPiece(tempSqr) != BoardPiece.EMPTY.value) {
                            if (BoardUtils.getPieceColor(boardStructure.getPiece(tempSqr)) ==
                                    (side ^ 1)) {
                                addCaptureMove(boardStructure, move(sqr, tempSqr,
                                        boardStructure.getPiece(tempSqr),
                                        BoardPiece.EMPTY.value, 0), moveList);
                            }
                            break;
                        }

                        addQuietMove(boardStructure, move(sqr, tempSqr, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, 0), moveList);
                        tempSqr += dir;
                    }
                }
            }

            piece = loopPieceSlides[pieceIndex++];
        }

        // Non slide pieces:
        pieceIndex = loopNonSlideIndex[side];
        piece = loopPieceNonSlides[pieceIndex++];
        while (piece != 0) {

            for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(piece); pieceNum++) {
                sqr = boardStructure.getPieceListEntry(piece, pieceNum);

                for (int i = 0; i < numDirections[piece]; i++) {
                    dir = pieceDirections[piece][i];
                    tempSqr = sqr + dir;

                    if (BoardUtils.isSquareOffBoard(boardStructure, tempSqr)) {
                        continue;
                    }

                    if (boardStructure.getPiece(tempSqr) != BoardPiece.EMPTY.value) {
                        if (BoardUtils.getPieceColor(boardStructure.getPiece(tempSqr)) ==
                                (side ^ 1)) {
                            addCaptureMove(boardStructure, move(sqr, tempSqr,
                                    boardStructure.getPiece(tempSqr),
                                    BoardPiece.EMPTY.value, 0), moveList);
                        }
                        continue;
                    }

                    addQuietMove(boardStructure, move(sqr, tempSqr, BoardPiece.EMPTY.value,
                            BoardPiece.EMPTY.value, 0), moveList);
                }
            }

            piece = loopPieceNonSlides[pieceIndex++];
        }
    }

    /**
     * Generate all valid capture moves.
     *
     * @param boardStructure
     * @param moveList
     */
    protected static void generateAllCaptureMoves(BoardStructure boardStructure,
                                                  MoveList moveList) {
        moveList.setCount(0);

        int piece = BoardPiece.EMPTY.value;
        int side = boardStructure.getSide();
        int sqr = 0;
        int tempSqr = 0;
        int pieceNum = 0;
        int dir = 0;
        int index = 0;
        int pieceIndex = 0;

        if (side == BoardColor.WHITE.value) {
            for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(BoardPiece.WHITE_PAWN.value);
                 ++pieceNum) {
                sqr = boardStructure.getPieceListEntry(BoardPiece.WHITE_PAWN.value, pieceNum);

                if (!BoardUtils.isSquareOffBoard(boardStructure, sqr + 9) &&
                        BoardUtils.getPieceColor(boardStructure.getPiece(sqr + 9)) ==
                                BoardColor.BLACK.value) {
                    addWhitePawnCaptureMove(boardStructure, sqr, sqr + 9,
                            boardStructure.getPiece(sqr + 9), moveList);
                }

                if (!BoardUtils.isSquareOffBoard(boardStructure, sqr + 11) &&
                        BoardUtils.getPieceColor(boardStructure.getPiece(sqr + 11)) ==
                                BoardColor.BLACK.value) {
                    addWhitePawnCaptureMove(boardStructure, sqr, sqr + 11,
                            boardStructure.getPiece(sqr + 11), moveList);
                }

                if (boardStructure.getEnPassant() != BoardSquare.NONE.value) {
                    if (sqr + 9 == boardStructure.getEnPassant()) {
                        addEnPassantMove(boardStructure, move(sqr, sqr + 9, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, MoveUtils.MOVE_FLAG_EN_PASSANT), moveList);
                    }

                    if (sqr + 11 == boardStructure.getEnPassant()) {
                        addEnPassantMove(boardStructure, move(sqr, sqr + 11, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, MoveUtils.MOVE_FLAG_EN_PASSANT), moveList);
                    }
                }
            }

        } else {
            for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(BoardPiece.BLACK_PAWN.value);
                 ++pieceNum) {
                sqr = boardStructure.getPieceListEntry(BoardPiece.BLACK_PAWN.value, pieceNum);

                if (!BoardUtils.isSquareOffBoard(boardStructure, sqr - 9) &&
                        BoardUtils.getPieceColor(boardStructure.getPiece(sqr - 9)) ==
                                BoardColor.WHITE.value) {
                    addBlackPawnCaptureMove(boardStructure, sqr, sqr - 9,
                            boardStructure.getPiece(sqr - 9), moveList);
                }

                if (!BoardUtils.isSquareOffBoard(boardStructure, sqr - 11) &&
                        BoardUtils.getPieceColor(boardStructure.getPiece(sqr - 11)) ==
                                BoardColor.WHITE.value) {
                    addBlackPawnCaptureMove(boardStructure, sqr, sqr - 11,
                            boardStructure.getPiece(sqr - 11), moveList);
                }

                if (boardStructure.getEnPassant() != BoardSquare.NONE.value) {
                    if (sqr - 9 == boardStructure.getEnPassant()) {
                        addEnPassantMove(boardStructure, move(sqr, sqr - 9, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, MoveUtils.MOVE_FLAG_EN_PASSANT), moveList);
                    }

                    if (sqr - 11 == boardStructure.getEnPassant()) {
                        addEnPassantMove(boardStructure, move(sqr, sqr - 11, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, MoveUtils.MOVE_FLAG_EN_PASSANT), moveList);
                    }
                }
            }

        }

        // Slide pieces:
        pieceIndex = loopSlideIndex[side];
        piece = loopPieceSlides[pieceIndex++];
        while (piece != 0) {

            for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(piece); pieceNum++) {
                sqr = boardStructure.getPieceListEntry(piece, pieceNum);

                for (int i = 0; i < numDirections[piece]; i++) {
                    dir = pieceDirections[piece][i];
                    tempSqr = sqr + dir;

                    while (!BoardUtils.isSquareOffBoard(boardStructure, tempSqr)) {
                        if (boardStructure.getPiece(tempSqr) != BoardPiece.EMPTY.value) {
                            if (BoardUtils.getPieceColor(boardStructure.getPiece(tempSqr)) ==
                                    (side ^ 1)) {
                                addCaptureMove(boardStructure, move(sqr, tempSqr,
                                        boardStructure.getPiece(tempSqr),
                                        BoardPiece.EMPTY.value, 0), moveList);
                            }
                            break;
                        }
                        tempSqr += dir;
                    }
                }
            }

            piece = loopPieceSlides[pieceIndex++];
        }

        // Non slide pieces:
        pieceIndex = loopNonSlideIndex[side];
        piece = loopPieceNonSlides[pieceIndex++];
        while (piece != 0) {

            for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(piece); pieceNum++) {
                sqr = boardStructure.getPieceListEntry(piece, pieceNum);

                for (int i = 0; i < numDirections[piece]; i++) {
                    dir = pieceDirections[piece][i];
                    tempSqr = sqr + dir;

                    if (BoardUtils.isSquareOffBoard(boardStructure, tempSqr)) {
                        continue;
                    }

                    if (boardStructure.getPiece(tempSqr) != BoardPiece.EMPTY.value) {
                        if (BoardUtils.getPieceColor(boardStructure.getPiece(tempSqr)) ==
                                (side ^ 1)) {
                            addCaptureMove(boardStructure, move(sqr, tempSqr,
                                    boardStructure.getPiece(tempSqr),
                                    BoardPiece.EMPTY.value, 0), moveList);
                        }
                        continue;
                    }
                }
            }

            piece = loopPieceNonSlides[pieceIndex++];
        }
    }
}
