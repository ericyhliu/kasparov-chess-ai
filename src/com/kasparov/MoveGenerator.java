package com.kasparov;

/**
 * Generates moves.
 */
public class MoveGenerator {

    public static int move(int from, int to, int captured, int promoted, int flag) {
        return from | ((to << 7) | (captured << 14)) | (promoted << 20) | flag;
    }

    public static boolean isSqrOffboard(int sqr) {
        return
    }


    public void moveGen(BoardStructure boardStructure, MoveList moveList) {
        // Loop all pieces
            // If slider, loop each dir and add move
                // AddMove list.moves[list.count] = move
                // list.count++
    }

    public void addQuietMove(BoardStructure boardStructure, int move, MoveList moveList) {
        moveList.addMove(move);
    }

    public void addCaptureMove(BoardStructure boardStructure, int move, MoveList moveList) {

    }

    public void addEnPassantMove(BoardStructure boardStructure, int move, MoveList moveList) {

    }

    public void addWhitePawnCaptureMove(BoardStructure boardStructure, int from, int to, int capture, MoveList moveList) {
        if (boardStructure.rankBoard[from] == BoardRank.RANK_7.value) {
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.WHITE_QUEEN.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.WHITE_ROOK.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.WHITE_BISHOP.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.WHITE_KNIGHT.value, 0), moveList);
        } else {
            addCaptureMove(boardStructure, move(from, to, capture, BoardPiece.EMPTY.value, 0), moveList);
        }
    }

    public void addWhitePawnMove(BoardStructure boardStructure, int from, int to, MoveList moveList) {
        if (boardStructure.rankBoard[from] == BoardRank.RANK_7.value) {
            addCaptureMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.WHITE_QUEEN.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.WHITE_ROOK.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.WHITE_BISHOP.value, 0), moveList);
            addCaptureMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.WHITE_KNIGHT.value, 0), moveList);
        } else {
            addCaptureMove(boardStructure, move(from, to, BoardPiece.EMPTY.value, BoardPiece.EMPTY.value, 0), moveList);
        }
    }

    public void generateAddMoves(BoardStructure boardStructure, MoveList moveList) {
        moveList.setCount(0);

        int piece = BoardPiece.EMPTY.value;
        int side = boardStructure.side;
        int sqr = 0;
        int tempSqr = 0;
        int pieceNum = 0;

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

        }



    }



}
