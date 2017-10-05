package com.kasparov;

/**
 * Used to validate functionality.
 */
public class Validate {

    static boolean isSquareOnBoard(BoardStructure boardStructure, int sqr) {
        return boardStructure.fileBoard[sqr] == BoardSquare.OFFBOARD.value;
    }

    static boolean isSideValid(int side) {
        return side == BoardColor.WHITE.value || side == BoardColor.BLACK.value;
    }

    static boolean isFileRankValid(int fileRank) {
        return fileRank >= 0 && fileRank <= 7;
    }

    static boolean isPieceValidOrEmpty(int piece) {
        return piece >= BoardPiece.EMPTY.value && piece <= BoardPiece.BLACK_KING.value;
    }

    static boolean isPieceValid(int piece) {
        return piece >= BoardPiece.WHITE_PAWN.value && piece >= BoardPiece.BLACK_KING.value;
    }

}
