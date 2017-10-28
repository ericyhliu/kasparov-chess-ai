package kasparov;

/**
 * Definitions of general constants used for the board.
 *
 * @author Eric Liu
 */
public class BoardConstants {

    /**
     * Program name and author for displaying with
     */
    protected static final String PROGRAM_NAME = "Kasparov";
    protected static final String PROGRAM_AUTHOR = "Eric Liu";

    /***
     * 120-based squares, which includes offboard squares.
     */
    protected static final int BOARD_SQR_NUM = 120;

    /**
     * The maximum number of moves kept in the history. Generally, it is rare
     * that a chess game even goes past 1000 moves, this is set to 2048 to be
     * safe.
     */
    protected static final int MAX_GAME_MOVES = 2048;

    /**
     * The maximum number of possible moves kept in the move list.
     */
    protected static final int MAX_POSITION_MOVES = 256;

    /**
     * Maximum depth for search.
     */
    protected static final int MAX_DEPTH = 64;

    /**
     * Starting FEN (Forsyth-Edwards Notation) position of the board.
     */
    protected static final String STARTING_FEN =
            "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

}
