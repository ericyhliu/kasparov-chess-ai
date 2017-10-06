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
    static final int MAX_POSITION_MOVES = 256;

    /**
     * Starting position of the board in Forsyth-Edwards Notation (FEN).
     */
    static final String startingFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    /**
     * Sample FEN states for testing.
     */
    static final String FEN1 = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
    static final String FEN2 = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";
    static final String FEN3 = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
    static final String FEN4 = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1";
    static final String FEN5 = "8/3q4/8/8/4Q3/8/8/8 w - - 0 2 ";
    static final String FEN6 = "rnbqkb1r/pp1p1pPp/8/2p1pP2/1P1P4/3P3P/P1P1P3/RNBQKBNR w KQkq e6 0 1";
    static final String FEN7 = "rnbqkbnr/p1p1p3/3p3p/1p1p4/2P1Pp2/8/PP1P1PpP/RNBQKBNR b - e3 0 1";

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
     * Array that maps piece to boolean for checking if it is a knight.
     */
    static final boolean[] pieceKnight = {
        false, // Empty piece
        false, true, false, false, false, false, // White pieces
        false, true, false, false, false, false  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a king.
     */
    static final boolean[] pieceKing = {
        false, // Empty piece
        false, false, false, false, false, true, // White pieces
        false, false, false, false, false, true  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a rook or queen.
     */
    static final boolean[] pieceRookOrQueen = {
        false, // Empty piece
        false, false, false, true, true, false, // White pieces
        false, false, false, true, true, false  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a bishop or queen.
     */
    static final boolean[] pieceBishopOrQueen = {
        false, // Empty piece
        false, false, true, false, true, false, // White pieces
        false, false, true, false, true, false  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if the piece is a slider piece.
     */
    static final boolean[] pieceSlides = {
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
    static int convertFileRankToSqr(int file, int rank) {
        return 21 + file + 10 * rank;
    }

    /**
     * Checks if piece is a knight.
     */
    static boolean isKnight(int piece) {
        return pieceKnight[piece];
    }

    /**
     * Checks if piece is a rook or queen.
     */
    static boolean isRookOrQueen(int piece) {
        return pieceRookOrQueen[piece];
    }

    /**
     * Checks if piece is a bishop or queen.
     */
    static boolean isBishopOrQueen(int piece) {
        return pieceBishopOrQueen[piece];
    }

    /**
     * Checks if piece is a king.
     */
    static boolean isKing(int piece) {
        return pieceKing[piece];
    }

}
