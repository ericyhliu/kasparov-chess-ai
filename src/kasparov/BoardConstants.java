package kasparov;

/**
 * Definitions of constants.
 *
 * @author Eric Liu
 */
public class BoardConstants {

    protected static final String PROGRAM_NAME = "Kasparov";
    protected static final String PROGRAM_AUTHOR = "Eric Liu";

    protected static final int BOARD_SQR_NUM = 120;
    protected static final int MAX_GAME_MOVES = 2048;
    protected static final int MAX_POSITION_MOVES = 256;
    protected static final int MAX_DEPTH = 64;
    protected static final int MATE = 29000;
    protected static final int NO_MOVE = 0;

    /**
     * Starting position of the board in Forsyth-Edwards Notation (FEN).
     */
    protected static final String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    /**
     * Sample FEN states for testing.
     */
    protected static final String FEN1 = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
    protected static final String FEN2 = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";
    protected static final String FEN3 = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
    protected static final String FEN4 = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
    protected static final String FEN5 = "8/3q4/8/8/4Q3/8/8/8 w - - 0 2 ";
    protected static final String FEN6 = "rnbqkb1r/pp1p1pPp/8/2p1pP2/1P1P4/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1";
    protected static final String FEN7 = "rnbqkbnr/p1p1p3/3p3p/1p1p4/2P1Pp2/8/PP1P1PpP/RNBQKBNR b - e3 0 1";
    protected static final String FEN8 = "5k2/1n6/4n3/6N1/8/3N4/8/5K2 b - - 0 1";
    protected static final String FEN9 = "5k2/1n6/4n3/6N1/8/3N4/8/5K2 w - - 0 1";
    protected static final String FEN10 = "6k1/8/5r2/8/1nR5/5N2/8/6K1 b - - 0 1";
    protected static final String FEN11 = "6k1/8/5r2/8/1nR5/5N2/8/6K1 w - - 0 1";
    protected static final String FEN12 = "6k1/8/4nq2/8/1nQ5/5N2/1N6/6K1 w - - 0 1 ";
    protected static final String FEN13 = "6k1/1b6/4n3/8/1n4B1/1B3N2/1N6/2b3K1 b - - 0 1 ";
    protected static final String FEN14 = "r3k2r/8/8/8/8/8/8/R3K2R b KQkq - 0 1";
    protected static final String FEN15 = "r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1";
    protected static final String FEN16 = "3rk2r/8/8/8/8/8/6p1/R3K2R b KQk - 0 1";
    protected static final String FEN17 = "3rk2r/8/8/8/8/8/6p1/R3K2R w KQk - 0 1";
    protected static final String FEN18 = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
    protected static final String FEN19 = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
    protected static final String FEN20 = "n1n5/PPPk4/8/8/8/8/4Kppp/5N1N w - - 0 1";
    protected static final String FEN21 = "2rr3k/pp3pp1/1nnqbN1p/3pN3/2pP4/2P3Q1/PPB4P/R4RK1 w - -";
    protected static final String FEN22 = "r1b1k2r/ppppnppp/2n2q2/2b5/3NP3/2P1B3/PP3PPP/RN1QKB1R w KQkq - 0 1";

    /**
     * Piece characters for printing.
     */
    protected static final String pieceChars = ".PNBRQKpnbrqk";

    /**
     * Side characters for printing.
     */
    protected static final String sideChars = "wb-";

    /**
     * Rank characters for printing.
     */
    protected static final String rankChars = "12345678";

    /**
     * File characters for printing.
     */
    protected static final String fileChars = "abcdefgh";

    /**
     * Array that maps piece to boolean for checking if it is a big piece.
     */
    protected static final boolean[] pieceBig = {
        false, // Empty piece
        false, true, true, true, true, true, // White pieces
        false, true, true, true, true, true  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a major piece.
     */
    protected static final boolean[] pieceMajor = {
        false, // Empty piece
        false, false, false, true, true, true, // White pieces
        false, false, false, true, true, true  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a minor piece.
     */
    protected static final boolean[] pieceMinor = {
        false, // Empty piece
        false, false, true, true, false, false, // White pieces
        false, false, true, true, false, false  // Black pieces
    };

    /**
     * Array that maps piece to value.
     */
    protected static final int[] pieceValue = {
        0, // Empty piece
        100, 325, 325, 550, 1000, 50000, // White pieces
        100, 325, 325, 550, 1000, 50000  // Black pieces
    };

    /**
     * Array that maps piece to color.
     */
    protected static final int[] pieceColor = {
        BoardColor.BOTH.value, // Empty piece
        BoardColor.WHITE.value, BoardColor.WHITE.value, BoardColor.WHITE.value, // White pieces
        BoardColor.WHITE.value, BoardColor.WHITE.value, BoardColor.WHITE.value,
        BoardColor.BLACK.value, BoardColor.BLACK.value, BoardColor.BLACK.value, // Black pieces
        BoardColor.BLACK.value, BoardColor.BLACK.value, BoardColor.BLACK.value
    };

    /**
     * Array that maps piece to boolean for checking if it is a pawn.
     */
    protected static final boolean[] piecePawn = {
        false, // Empty piece
        true, false, false, false, false, false, // White pieces
        true, false, false, false, false, false  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a knight.
     */
    protected static final boolean[] pieceKnight = {
        false, // Empty piece
        false, true, false, false, false, false, // White pieces
        false, true, false, false, false, false  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a king.
     */
    protected static final boolean[] pieceKing = {
        false, // Empty piece
        false, false, false, false, false, true, // White pieces
        false, false, false, false, false, true  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a rook or queen.
     */
    protected static final boolean[] pieceRookOrQueen = {
        false, // Empty piece
        false, false, false, true, true, false, // White pieces
        false, false, false, true, true, false  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a bishop or queen.
     */
    protected static final boolean[] pieceBishopOrQueen = {
        false, // Empty piece
        false, false, true, false, true, false, // White pieces
        false, false, true, false, true, false  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if the piece is a slider piece.
     */
    protected static final boolean[] pieceSlides = {
            false, // Empty piece
            false, false, true, true, true, false, // White pieces
            false, false, true, true, true, false  // White pieces
    };


    /**
     * Converts a file and rank value to a square value.
     *
     * @param file
     * @param rank
     */
    protected static int convertFileRankToSqr(int file, int rank) {
        return 21 + file + 10 * rank;
    }

    /**
     * Checks if piece is a knight.
     */
    protected static boolean isKnight(int piece) {
        return pieceKnight[piece];
    }

    /**
     * Checks if piece is a rook or queen.
     */
    protected static boolean isRookOrQueen(int piece) {
        return pieceRookOrQueen[piece];
    }

    /**
     * Checks if piece is a bishop or queen.
     */
    protected static boolean isBishopOrQueen(int piece) {
        return pieceBishopOrQueen[piece];
    }

    /**
     * Checks if piece is a king.
     */
    protected static boolean isKing(int piece) {
        return pieceKing[piece];
    }

}
