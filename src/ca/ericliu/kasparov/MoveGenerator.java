package ca.ericliu.kasparov;

/**
 * Generates moves.
 */
public class MoveGenerator {

    /**
     * Array of slider pieces.
     */
    static final int[] loopPieceSlides = {
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
    static final int[] loopPieceNonSlides = {
        BoardPiece.WHITE_KNIGHT.value,
        BoardPiece.WHITE_KING.value,
        0,
        BoardPiece.BLACK_KNIGHT.value,
        BoardPiece.BLACK_KING.value,
        0
    };

    static final int[] loopSlideIndex = {
        0, 4
    };

    static final int[] loopNonSlideIndex = {
        0, 3
    };

    static final int[][] pieceDirections = {
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

    static final int[] numDirections = {
        0, 0, 8, 4, 4, 8, 8, 0, 8, 4, 4, 8, 8
    };


    static final int[] victimScore = {
        0, // Empty piece
        100, 200, 300, 400, 500, 600, // White pieces
        100, 200, 300, 400, 500, 600  // Black pieces
    };

    static int[][] mvvLva = new int[13][13];


    static void initMvvLva() {
        int attacker;
        int victim;
        for (attacker = BoardPiece.WHITE_PAWN.value; attacker <= BoardPiece.BLACK_KING.value; attacker++) {
            for (victim = BoardPiece.WHITE_PAWN.value; victim <= BoardPiece.BLACK_KING.value; victim++) {
                mvvLva[victim][attacker] = victimScore[victim] + 6 - (victimScore[attacker]/100);
            }
        }
    }


    public static boolean moveExists(BoardStructure boardStructure, int move) {
        MoveList moveList = new MoveList();
        MoveGenerator.generateAllMoves(boardStructure, moveList);
        for (int moveNum = 0; moveNum < moveList.count; moveNum++) {
            if (!MakeMove.makeMove(boardStructure, moveList.moves[moveNum].move))
                continue;
            MakeMove.takeMove(boardStructure);
            if (moveList.moves[moveNum].move == move)
                return true;
        }
        return false;
    }

    public static int move(int from, int to, int captured, int promoted, int flag) {
        return from | ((to << 7) | (captured << 14)) | (promoted << 20) | flag;
    }

    public static void addQuietMove(BoardStructure boardStructure, int move, MoveList moveList) {
        moveList.moves[moveList.count] = new Move();
        moveList.moves[moveList.count].move = move;

        if (boardStructure.searchKillers[0][boardStructure.ply] == move) {
            moveList.moves[moveList.count].score = 900000;
        } else if (boardStructure.searchKillers[1][boardStructure.ply] == move) {
            moveList.moves[moveList.count].score = 800000;
        } else {
            moveList.moves[moveList.count].score = boardStructure
                    .searchHistory[boardStructure.pieces[Move.from(move)]][Move.to(move)];
        }
        moveList.count++;
    }

    public static void addCaptureMove(BoardStructure boardStructure, int move, MoveList moveList) {
        moveList.moves[moveList.count] = new Move();
        moveList.moves[moveList.count].move = move;
        moveList.moves[moveList.count].score = mvvLva[Move.captured(move)][boardStructure.pieces[Move.from(move)]] +
            1000000;
        moveList.count++;
    }

    public static void addEnPassantMove(BoardStructure boardStructure, int move, MoveList moveList) {
        moveList.moves[moveList.count] = new Move();
        moveList.moves[moveList.count].move = move;
        moveList.moves[moveList.count].score = 105 + 1000000;
        moveList.count++;
    }

    public static void addWhitePawnCaptureMove(BoardStructure boardStructure, int from, int to, int capture, MoveList moveList) {
        if (boardStructure.rankBoard[from] == BoardRank.RANK_7.value) {
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.WHITE_QUEEN.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.WHITE_ROOK.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.WHITE_BISHOP.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.WHITE_KNIGHT.value, 0), moveList);
        } else {
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.EMPTY.value, 0), moveList);
        }
    }

    public static void addWhitePawnMove(BoardStructure boardStructure, int from, int to, MoveList moveList) {
        if (boardStructure.rankBoard[from] == BoardRank.RANK_7.value) {
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.WHITE_QUEEN.value, 0), moveList);
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.WHITE_ROOK.value, 0), moveList);
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.WHITE_BISHOP.value, 0), moveList);
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.WHITE_KNIGHT.value, 0), moveList);
        } else {
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.EMPTY.value, 0), moveList);
        }
    }

    public static void addBlackPawnCaptureMove(BoardStructure boardStructure, int from, int to, int capture, MoveList moveList) {
        if (boardStructure.rankBoard[from] == BoardRank.RANK_2.value) {
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.BLACK_QUEEN.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.BLACK_ROOK.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.BLACK_BISHOP.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.BLACK_KNIGHT.value, 0), moveList);
        } else {
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.EMPTY.value, 0), moveList);
        }
    }

    public static void addBlackPawnMove(BoardStructure boardStructure, int from, int to, MoveList moveList) {
        if (boardStructure.rankBoard[from] == BoardRank.RANK_2.value) {
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.BLACK_QUEEN.value, 0), moveList);
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.BLACK_ROOK.value, 0), moveList);
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.BLACK_BISHOP.value, 0), moveList);
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.BLACK_KNIGHT.value, 0), moveList);
        } else {
            addQuietMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.EMPTY.value, 0), moveList);
        }
    }

    public static void generateAllMoves(BoardStructure boardStructure, MoveList moveList) {
        moveList.setCount(0);

        int piece = BoardPiece.EMPTY.value;
        int side = boardStructure.side;
        int sqr = 0;
        int tempSqr = 0;
        int pieceNum = 0;
        int dir = 0;
        int index = 0;
        int pieceIndex = 0;

        // System.out.println("Side: " + side);

        if (side == BoardColor.WHITE.value) {
            for (pieceNum = 0; pieceNum < boardStructure.pieceNum[BoardPiece.WHITE_PAWN.value]; ++pieceNum) {
                sqr = boardStructure.pieceList[BoardPiece.WHITE_PAWN.value][pieceNum];
                assert(Validate.isSquareOnBoard(boardStructure, sqr));

                if (boardStructure.pieces[sqr + 10] == BoardPiece.EMPTY.value) {
                    addWhitePawnMove(boardStructure, sqr, sqr + 10, moveList);

                    if (boardStructure.rankBoard[sqr] == BoardRank.RANK_2.value && boardStructure.pieces[sqr + 20] == BoardPiece.EMPTY.value) {
                        addQuietMove(boardStructure, move(sqr, sqr + 20, BoardPiece.EMPTY.value, BoardPiece.EMPTY.value, Move.moveFlagPawnStart), moveList);
                    }
                }

                if (!Validate.isSquareOffboard(boardStructure, sqr + 9) &&
                    BoardConstants.pieceColor[boardStructure.pieces[sqr + 9]] == BoardColor.BLACK.value) {
                    addWhitePawnCaptureMove(boardStructure, sqr, sqr + 9, boardStructure.pieces[sqr + 9], moveList);
                }

                if (!Validate.isSquareOffboard(boardStructure, sqr + 11) &&
                    BoardConstants.pieceColor[boardStructure.pieces[sqr + 11]] == BoardColor.BLACK.value) {
                    addWhitePawnCaptureMove(boardStructure, sqr, sqr + 11, boardStructure.pieces[sqr + 11], moveList);
                }

                if (boardStructure.enPassant != BoardSquare.NONE.value) {
                    if (sqr + 9 == boardStructure.enPassant) {
                        addEnPassantMove(boardStructure, move(sqr, sqr + 9, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, Move.moveFlagEnPassant), moveList);
                    }

                    if (sqr + 11 == boardStructure.enPassant) {
                        addEnPassantMove(boardStructure, move(sqr, sqr + 11, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, Move.moveFlagEnPassant), moveList);
                    }
                }
            }

            if ((boardStructure.castlePerm & BoardCastleLink.WHITE_KING_CASTLE.value) != 0) {
                if (boardStructure.pieces[BoardSquare.F1.value] == BoardPiece.EMPTY.value &&
                    boardStructure.pieces[BoardSquare.G1.value] == BoardPiece.EMPTY.value) {
                    if (!SquareAttacked.squareAttacked(BoardSquare.E1.value, BoardColor.BLACK.value, boardStructure) &&
                        !SquareAttacked.squareAttacked(BoardSquare.F1.value, BoardColor.BLACK.value, boardStructure)) {
                        // System.out.println("WKCA Move Gen: ");
                        addQuietMove(boardStructure, move(BoardSquare.E1.value, BoardSquare.G1.value,
                                BoardPiece.EMPTY.value, BoardPiece.EMPTY.value, Move.moveFlagCastle), moveList);
                    }
                }
            }

            if ((boardStructure.castlePerm & BoardCastleLink.WHITE_QUEEN_CASTLE.value) != 0) {
                if (boardStructure.pieces[BoardSquare.D1.value] == BoardPiece.EMPTY.value &&
                    boardStructure.pieces[BoardSquare.C1.value] == BoardPiece.EMPTY.value &&
                    boardStructure.pieces[BoardSquare.B1.value] == BoardPiece.EMPTY.value) {
                    if (!SquareAttacked.squareAttacked(BoardSquare.E1.value, BoardColor.BLACK.value, boardStructure) &&
                        !SquareAttacked.squareAttacked(BoardSquare.D1.value, BoardColor.BLACK.value, boardStructure)) {
                        // System.out.println("WQCA Move Gen: ");
                        addQuietMove(boardStructure, move(BoardSquare.E1.value, BoardSquare.C1.value,
                                BoardPiece.EMPTY.value, BoardPiece.EMPTY.value, Move.moveFlagCastle), moveList);
                    }
                }
            }

        } else {
            for (pieceNum = 0; pieceNum < boardStructure.pieceNum[BoardPiece.BLACK_PAWN.value]; ++pieceNum) {
                sqr = boardStructure.pieceList[BoardPiece.BLACK_PAWN.value][pieceNum];
                assert(Validate.isSquareOnBoard(boardStructure, sqr));

                if (boardStructure.pieces[sqr - 10] == BoardPiece.EMPTY.value) {
                    addBlackPawnMove(boardStructure, sqr, sqr - 10, moveList);

                    if (boardStructure.rankBoard[sqr] == BoardRank.RANK_7.value && boardStructure.pieces[sqr - 20] == BoardPiece.EMPTY.value) {
                        addQuietMove(boardStructure, move(sqr, sqr - 20, BoardPiece.EMPTY.value, BoardPiece.EMPTY.value, Move.moveFlagPawnStart), moveList);
                    }
                }

                if (!Validate.isSquareOffboard(boardStructure, sqr - 9) &&
                        BoardConstants.pieceColor[boardStructure.pieces[sqr - 9]] == BoardColor.WHITE.value) {
                    addBlackPawnCaptureMove(boardStructure, sqr, sqr - 9, boardStructure.pieces[sqr - 9], moveList);
                }

                if (!Validate.isSquareOffboard(boardStructure, sqr - 11) &&
                        BoardConstants.pieceColor[boardStructure.pieces[sqr - 11]] == BoardColor.WHITE.value) {
                    addBlackPawnCaptureMove(boardStructure, sqr, sqr - 11, boardStructure.pieces[sqr - 11], moveList);
                }

                if (boardStructure.enPassant != BoardSquare.NONE.value) {
                    if (sqr - 9 == boardStructure.enPassant) {
                        addEnPassantMove(boardStructure, move(sqr, sqr - 9, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, Move.moveFlagEnPassant), moveList);
                    }

                    if (sqr - 11 == boardStructure.enPassant) {
                        addEnPassantMove(boardStructure, move(sqr, sqr - 11, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, Move.moveFlagEnPassant), moveList);
                    }
                }
            }

            if ((boardStructure.castlePerm & BoardCastleLink.BLACK_KING_CASTLE.value) != 0) {
                if (boardStructure.pieces[BoardSquare.F8.value] == BoardPiece.EMPTY.value &&
                    boardStructure.pieces[BoardSquare.G8.value] == BoardPiece.EMPTY.value) {
                    if (!SquareAttacked.squareAttacked(BoardSquare.E8.value, BoardColor.WHITE.value, boardStructure) &&
                        !SquareAttacked.squareAttacked(BoardSquare.F8.value, BoardColor.WHITE.value, boardStructure)) {
                        // System.out.println("BKCA Move Gen: ");
                        addQuietMove(boardStructure, move(BoardSquare.E8.value, BoardSquare.G8.value,
                                BoardPiece.EMPTY.value, BoardPiece.EMPTY.value, Move.moveFlagCastle), moveList);
                    }
                }
            }

            if ((boardStructure.castlePerm & BoardCastleLink.BLACK_QUEEN_CASTLE.value) != 0) {
                if (boardStructure.pieces[BoardSquare.D8.value] == BoardPiece.EMPTY.value &&
                    boardStructure.pieces[BoardSquare.C8.value] == BoardPiece.EMPTY.value &&
                    boardStructure.pieces[BoardSquare.B8.value] == BoardPiece.EMPTY.value) {
                    if (!SquareAttacked.squareAttacked(BoardSquare.E8.value, BoardColor.WHITE.value, boardStructure) &&
                        !SquareAttacked.squareAttacked(BoardSquare.D8.value, BoardColor.WHITE.value, boardStructure)) {
                        // System.out.println("BQCA Move Gen: ");
                        addQuietMove(boardStructure, move(BoardSquare.E8.value, BoardSquare.C8.value,
                                BoardPiece.EMPTY.value, BoardPiece.EMPTY.value, Move.moveFlagCastle), moveList);
                    }
                }
            }

        }

        // Slide pieces:
        pieceIndex = loopSlideIndex[side];
        piece = loopPieceSlides[pieceIndex++];
        while (piece != 0) {
            // System.out.println("Sliders Piece Index: " + pieceIndex + "   Piece: " + piece);

            for (pieceNum = 0; pieceNum < boardStructure.pieceNum[piece]; pieceNum++) {
                sqr = boardStructure.pieceList[piece][pieceNum];
                // System.out.print("Piece: " + BoardConstants.pieceChars.charAt(piece) + " on ");
                // boardStructure.printSqr(sqr);

                for (int i = 0; i < numDirections[piece]; i++) {
                    dir = pieceDirections[piece][i];
                    tempSqr = sqr + dir;

                    while (!Validate.isSquareOffboard(boardStructure, tempSqr)) {
                        if (boardStructure.pieces[tempSqr] != BoardPiece.EMPTY.value) {
                            if (BoardConstants.pieceColor[boardStructure.pieces[tempSqr]] == (side ^ 1)) {
                                // System.out.print("Capture on: " );
                                // boardStructure.printSqr(tempSqr);
                                addCaptureMove(boardStructure, move(sqr, tempSqr, boardStructure.pieces[tempSqr],
                                        BoardPiece.EMPTY.value, 0), moveList);
                            }
                            break;
                        }

                        // System.out.print("Normal on: " );
                        // boardStructure.printSqr(tempSqr);
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
            // System.out.println("Non Sliders Piece Index: " + pieceIndex + "   Piece: " + piece);

            for (pieceNum = 0; pieceNum < boardStructure.pieceNum[piece]; pieceNum++) {
                sqr = boardStructure.pieceList[piece][pieceNum];
                // System.out.print("Piece: " + BoardConstants.pieceChars.charAt(piece) + " on ");
                // boardStructure.printSqr(sqr);

                for (int i = 0; i < numDirections[piece]; i++) {
                    dir = pieceDirections[piece][i];
                    tempSqr = sqr + dir;

                    if (Validate.isSquareOffboard(boardStructure, tempSqr)) {
                        continue;
                    }

                    if (boardStructure.pieces[tempSqr] != BoardPiece.EMPTY.value) {
                        if (BoardConstants.pieceColor[boardStructure.pieces[tempSqr]] == (side ^ 1)) {
                            // System.out.print("Capture on: " );
                            // boardStructure.printSqr(tempSqr);
                            addCaptureMove(boardStructure, move(sqr, tempSqr, boardStructure.pieces[tempSqr],
                                    BoardPiece.EMPTY.value, 0), moveList);
                        }
                        continue;
                    }

                    // System.out.print("Normal on: " );
                    // boardStructure.printSqr(tempSqr);
                    addQuietMove(boardStructure, move(sqr, tempSqr, BoardPiece.EMPTY.value,
                            BoardPiece.EMPTY.value, 0), moveList);
                }
            }

            piece = loopPieceNonSlides[pieceIndex++];
        }
    }

    public static void generateAllCaptureMoves(BoardStructure boardStructure, MoveList moveList) {
        moveList.setCount(0);

        int piece = BoardPiece.EMPTY.value;
        int side = boardStructure.side;
        int sqr = 0;
        int tempSqr = 0;
        int pieceNum = 0;
        int dir = 0;
        int index = 0;
        int pieceIndex = 0;

        // System.out.println("Side: " + side);

        if (side == BoardColor.WHITE.value) {
            for (pieceNum = 0; pieceNum < boardStructure.pieceNum[BoardPiece.WHITE_PAWN.value]; ++pieceNum) {
                sqr = boardStructure.pieceList[BoardPiece.WHITE_PAWN.value][pieceNum];
                assert(Validate.isSquareOnBoard(boardStructure, sqr));

                if (!Validate.isSquareOffboard(boardStructure, sqr + 9) &&
                        BoardConstants.pieceColor[boardStructure.pieces[sqr + 9]] == BoardColor.BLACK.value) {
                    addWhitePawnCaptureMove(boardStructure, sqr, sqr + 9, boardStructure.pieces[sqr + 9], moveList);
                }

                if (!Validate.isSquareOffboard(boardStructure, sqr + 11) &&
                        BoardConstants.pieceColor[boardStructure.pieces[sqr + 11]] == BoardColor.BLACK.value) {
                    addWhitePawnCaptureMove(boardStructure, sqr, sqr + 11, boardStructure.pieces[sqr + 11], moveList);
                }

                if (boardStructure.enPassant != BoardSquare.NONE.value) {
                    if (sqr + 9 == boardStructure.enPassant) {
                        addEnPassantMove(boardStructure, move(sqr, sqr + 9, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, Move.moveFlagEnPassant), moveList);
                    }

                    if (sqr + 11 == boardStructure.enPassant) {
                        addEnPassantMove(boardStructure, move(sqr, sqr + 11, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, Move.moveFlagEnPassant), moveList);
                    }
                }
            }

        } else {
            for (pieceNum = 0; pieceNum < boardStructure.pieceNum[BoardPiece.BLACK_PAWN.value]; ++pieceNum) {
                sqr = boardStructure.pieceList[BoardPiece.BLACK_PAWN.value][pieceNum];
                assert(Validate.isSquareOnBoard(boardStructure, sqr));

                if (!Validate.isSquareOffboard(boardStructure, sqr - 9) &&
                        BoardConstants.pieceColor[boardStructure.pieces[sqr - 9]] == BoardColor.WHITE.value) {
                    addBlackPawnCaptureMove(boardStructure, sqr, sqr - 9, boardStructure.pieces[sqr - 9], moveList);
                }

                if (!Validate.isSquareOffboard(boardStructure, sqr - 11) &&
                        BoardConstants.pieceColor[boardStructure.pieces[sqr - 11]] == BoardColor.WHITE.value) {
                    addBlackPawnCaptureMove(boardStructure, sqr, sqr - 11, boardStructure.pieces[sqr - 11], moveList);
                }

                if (boardStructure.enPassant != BoardSquare.NONE.value) {
                    if (sqr - 9 == boardStructure.enPassant) {
                        addEnPassantMove(boardStructure, move(sqr, sqr - 9, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, Move.moveFlagEnPassant), moveList);
                    }

                    if (sqr - 11 == boardStructure.enPassant) {
                        addEnPassantMove(boardStructure, move(sqr, sqr - 11, BoardPiece.EMPTY.value,
                                BoardPiece.EMPTY.value, Move.moveFlagEnPassant), moveList);
                    }
                }
            }

        }

        // Slide pieces:
        pieceIndex = loopSlideIndex[side];
        piece = loopPieceSlides[pieceIndex++];
        while (piece != 0) {

            for (pieceNum = 0; pieceNum < boardStructure.pieceNum[piece]; pieceNum++) {
                sqr = boardStructure.pieceList[piece][pieceNum];

                for (int i = 0; i < numDirections[piece]; i++) {
                    dir = pieceDirections[piece][i];
                    tempSqr = sqr + dir;

                    while (!Validate.isSquareOffboard(boardStructure, tempSqr)) {
                        if (boardStructure.pieces[tempSqr] != BoardPiece.EMPTY.value) {
                            if (BoardConstants.pieceColor[boardStructure.pieces[tempSqr]] == (side ^ 1)) {
                                addCaptureMove(boardStructure, move(sqr, tempSqr, boardStructure.pieces[tempSqr],
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

            for (pieceNum = 0; pieceNum < boardStructure.pieceNum[piece]; pieceNum++) {
                sqr = boardStructure.pieceList[piece][pieceNum];

                for (int i = 0; i < numDirections[piece]; i++) {
                    dir = pieceDirections[piece][i];
                    tempSqr = sqr + dir;

                    if (Validate.isSquareOffboard(boardStructure, tempSqr)) {
                        continue;
                    }

                    if (boardStructure.pieces[tempSqr] != BoardPiece.EMPTY.value) {
                        if (BoardConstants.pieceColor[boardStructure.pieces[tempSqr]] == (side ^ 1)) {
                            addCaptureMove(boardStructure, move(sqr, tempSqr, boardStructure.pieces[tempSqr],
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
