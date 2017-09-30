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
     * Converts a file and rank value to a square value.
     *
     * @param file
     * @param rank
     */
    static int convertFileRankToSqr(int file, int rank) {
        return 21 + file + 10 * rank;
    }

}
