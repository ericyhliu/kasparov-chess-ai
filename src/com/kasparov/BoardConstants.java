package com.kasparov;

/**
 * Definitions of constants.
 *
 * @author Eric Liu
 */
public class BoardConstants {

    static final String PROGRAM_NAME = "Kasparov Chess AI";
    static final String PROGRAM_AUTHOR = "Eric Liu";

    static final int BOARD_SQR_NUM = 120;
    static final int MAX_GAME_MOVES = 2048;

    /**
     * Starting position of the board in Forsyth-Edwards Notation (FEN).
     */
    static final String startingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    /**
     * Sample FEN states.
     */
    static final String FEN2 = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
    static final String FEN3 = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";
    static final String FEN4 = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";

    /**
     * Piece characters for printing.
     */
    static final String pieceChars = ".PNBRQKpnbrqk";

    /**
     * Side characters for printing.
     */
    static final String sideChars = "wb-";

    /**
     * Rank characters for printing.
     */
    static final String rankChars = "12345678";

    /**
     * File characters for printing.
     */
    static final String fileChars = "abcdefgh";

    /**
     * Array that maps piece to boolean for checking if it is a big piece.
     */
    static final boolean[] pieceBig = {
        false, // Empty piece
        false, true, true, true, true, true, // White pieces
        false, true, true, true, true, true  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a major piece.
     */
    static final boolean[] pieceMajor = {
        false, // Empty piece
        false, false, false, true, true, true, // White pieces
        false, false, false, true, true, true  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a minor piece.
     */
    static final boolean[] pieceMinor = {
        false, // Empty piece
        false, false, true, true, false, false, // White pieces
        false, false, true, true, false, false  // Black pieces
    };

    /**
     * Array that maps piece to value.
     */
    static final int[] pieceValue = {
        0, // Empty piece
        100, 325, 325, 550, 1000, 50000, // White pieces
        100, 325, 325, 550, 1000, 50000  // Black pieces
    };

    /**
     * Array that maps piece to color.
     */
    static final int[] pieceColor = {
        BoardColor.BOTH.value, // Empty piece
        BoardColor.WHITE.value, BoardColor.WHITE.value, BoardColor.WHITE.value, // White pieces
        BoardColor.WHITE.value, BoardColor.WHITE.value, BoardColor.WHITE.value,
        BoardColor.BLACK.value, BoardColor.BLACK.value, BoardColor.BLACK.value, // Black pieces
        BoardColor.BLACK.value, BoardColor.BLACK.value, BoardColor.BLACK.value
    };

    /**
     * Converts a file and rank value to a square value.
     *
     * @param file
     * @param rank
     */
    static int convertFileRankToSqr(int file, int rank) {
        return 21 + file + 10 * rank;
    }

}
