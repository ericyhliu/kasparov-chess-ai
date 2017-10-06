package com.kasparov;

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


    public static int move(int from, int to, int captured, int promoted, int flag) {
        return from | ((to << 7) | (captured << 14)) | (promoted << 20) | flag;
    }

    public void moveGen(BoardStructure boardStructure, MoveList moveList) {
        // Loop all pieces
            // If slider, loop each dir and add move
                // AddMove list.moves[list.count] = move
                // list.count++
    }

    public static void addQuietMove(BoardStructure boardStructure, int move, MoveList moveList) {
        moveList.addMove(move);
    }

    public static void addCaptureMove(BoardStructure boardStructure, int move, MoveList moveList) {
        moveList.addMove(move);
    }

    public static void addEnPassantMove(BoardStructure boardStructure, int move, MoveList moveList) {
        moveList.addMove(move);
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

    public static void generateAddMoves(BoardStructure boardStructure, MoveList moveList) {
        moveList.setCount(0);

        int piece = BoardPiece.EMPTY.value;
        int side = boardStructure.side;
        int sqr = 0;
        int tempSqr = 0;
        int pieceNum = 0;
        int dir = 0;
        int index = 0;
        int pieceIndex = 0;

        System.out.println("Side: " + side);

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

                if (sqr + 9 == boardStructure.enPassant) {
                    addCaptureMove(boardStructure, move(sqr, sqr + 9, BoardPiece.EMPTY.value,
                            BoardPiece.EMPTY.value, Move.moveFlagEnPassant), moveList);
                }

                if (sqr + 11 == boardStructure.enPassant) {
                    addCaptureMove(boardStructure, move(sqr, sqr + 11, BoardPiece.EMPTY.value,
                            BoardPiece.EMPTY.value, Move.moveFlagEnPassant), moveList);
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

                if (sqr - 9 == boardStructure.enPassant) {
                    addCaptureMove(boardStructure, move(sqr, sqr - 9, BoardPiece.EMPTY.value,
                            BoardPiece.EMPTY.value, Move.moveFlagEnPassant), moveList);
                }

                if (sqr - 11 == boardStructure.enPassant) {
                    addCaptureMove(boardStructure, move(sqr, sqr - 11, BoardPiece.EMPTY.value,
                            BoardPiece.EMPTY.value, Move.moveFlagEnPassant), moveList);
                }

            }
        }

        // Slide pieces:
        pieceIndex = loopSlideIndex[side];
        piece = loopPieceSlides[pieceIndex++];
        while (piece != 0) {
            System.out.println("Sliders Piece Index: " + pieceIndex + "   Piece: " + piece);
            piece = loopPieceSlides[pieceIndex++];
        }

        // Non slide pieces:
        pieceIndex = loopNonSlideIndex[side];
        piece = loopPieceNonSlides[pieceIndex++];
        while (piece != 0) {
            System.out.println("Non Sliders Piece Index: " + pieceIndex + "   Piece: " + piece);
            piece = loopPieceNonSlides[pieceIndex++];
        }


    }



}
