package kasparov;

/**
 * General utility functions for the board.
 *
 * @author Eric Liu
 */
public class BoardUtils {

    /**
     * Array that maps piece to boolean for checking if it is a pawn.
     */
    private static final boolean[] isPiecePawn = {
        false, // Empty piece
        true, false, false, false, false, false, // White pieces
        true, false, false, false, false, false  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a knight.
     */
    private static final boolean[] isPieceKnight = {
        false, // Empty piece
        false, true, false, false, false, false, // White pieces
        false, true, false, false, false, false  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a rook or queen.
     */
    private static final boolean[] isPieceRookOrQueen = {
        false, // Empty piece
        false, false, false, true, true, false, // White pieces
        false, false, false, true, true, false  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a bishop or
     * queen.
     */
    private static final boolean[] isPieceBishopOrQueen = {
        false, // Empty piece
        false, false, true, false, true, false, // White pieces
        false, false, true, false, true, false  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a king.
     */
    private static final boolean[] isPieceKing = {
            false, // Empty piece
            false, false, false, false, false, true, // White pieces
            false, false, false, false, false, true  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if the piece is a slider
     * piece (bishop, rook, queen).
     */
    private static final boolean[] isPieceSlider = {
        false, // Empty piece
        false, false, true, true, true, false, // White pieces
        false, false, true, true, true, false  // White pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a big piece
     * (knight, bishop, rook, queen, king).
     */
    private static final boolean[] isPieceBig = {
        false, // Empty piece
        false, true, true, true, true, true, // White pieces
        false, true, true, true, true, true  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a major piece,
     * (rook, queen, king).
     */
    private static final boolean[] isPieceMajor = {
        false, // Empty piece
        false, false, false, true, true, true, // White pieces
        false, false, false, true, true, true  // Black pieces
    };

    /**
     * Array that maps piece to boolean for checking if it is a minor piece,
     * (bishop, knight).
     */
    private static final boolean[] isPieceMinor = {
        false, // Empty piece
        false, true, true, false, false, false, // White pieces
        false, true, true, false, false, false  // Black pieces
    };

    /**
     * Piece characters for printing.
     */
    private static final String pieceChars = ".PNBRQKpnbrqk";

    /**
     * Side characters for printing.
     */
    private static final String sideChars = "wb-";

    /**
     * File characters for printing.
     */
    private static final String fileChars = "abcdefgh";

    /**
     * Rank characters for printing.
     */
    private static final String rankChars = "12345678";

    /**
     * Array that maps piece to value.
     */
    private static final int[] pieceValue = {
        0, // Empty piece
        100, 325, 325, 550, 1000, 50000, // White pieces
        100, 325, 325, 550, 1000, 50000  // Black pieces
    };

    /**
     * Array that maps piece to color.
     */
    private static final int[] pieceColor = {
        BoardColor.BOTH.value, // Empty piece
        BoardColor.WHITE.value, BoardColor.WHITE.value,
        BoardColor.WHITE.value, BoardColor.WHITE.value,
        BoardColor.WHITE.value, BoardColor.WHITE.value,
        BoardColor.BLACK.value, BoardColor.BLACK.value,
        BoardColor.BLACK.value, BoardColor.BLACK.value,
        BoardColor.BLACK.value, BoardColor.BLACK.value
    };

    /**
     * Array that maps 120-based square to castle permissions.
     */
    private static int[] castlePerm = {
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 13, 15, 15, 15, 12, 15, 15, 14, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15,  7, 15, 15, 15,  3, 15, 15, 11, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15
    };

    /**
     * Empty move.
     */
    protected static final int NO_MOVE = 0;

    /**
     * Checks if a piece is a pawn.
     *
     * @param piece
     * @return true if the piece is a pawn, false otherwise
     * @throws IllegalArgumentException if the piece is invalid
     */
    protected static boolean isPiecePawn(int piece) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("Invalid piece");
        return isPiecePawn[piece];
    }

    /**
     * Checks if the piece is a knight.
     *
     * @param piece
     * @return true if the piece is a knight, false otherwise
     * @throws IllegalArgumentException if the piece is invalid
     */
    protected static boolean isPieceKnight(int piece) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("Invalid piece");
        return isPieceKnight[piece];
    }

    /**
     * Checks if the piece is a rook or queen.
     *
     * @param piece
     * @return true if the piece is a rook or queen, false otherwise
     * @throws IllegalArgumentException if the piece is invalid
     */
    protected static boolean isPieceRookOrQueen(int piece) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("Invalid piece");
        return isPieceRookOrQueen[piece];
    }

    /**
     * Checks if the piece is a bishop or queen.
     *
     * @param piece
     * @return true if the piece is a bishop or queen, false otherwise
     * @throws IllegalArgumentException if the piece is invalid
     */
    protected static boolean isPieceBishopOrQueen(int piece) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("Invalid piece");
        return isPieceBishopOrQueen[piece];
    }

    /**
     * Checks if the piece is a king.
     *
     * @param piece
     * @return true if the piece is a king, false otherwise
     * @throws IllegalArgumentException if the piece is invalid
     */
    protected static boolean isPieceKing(int piece) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("Invalid piece");
        return isPieceKing[piece];
    }

    /**
     * Checks if the piece is a slider piece.
     *
     * @param piece
     * @return true if the piece is a slider piece, false otherwise
     * @throws IllegalArgumentException if the piece is invalid
     */
    protected static boolean isPieceSlider(int piece) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("Invalid piece");
        return isPieceSlider[piece];
    }

    /**
     * Checks if the piece is a big piece (knight, bishop, rook, queen, king).
     *
     * @param piece
     * @return true if the piece is a big piece, false otherwise
     * @throws IllegalArgumentException if the piece is invalid
     */
    protected static boolean isPieceBig(int piece) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("Invalid piece");
        return isPieceBig[piece];
    }

    /**
     * Checks if the piece is a major piece (rook, queen, king).
     *
     * @param piece
     * @return true if the piece is a major piece, false otherwise
     * @throws IllegalArgumentException if the piece is invalid
     */
    protected static boolean isPieceMajor(int piece) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("Invalid piece");
        return isPieceMajor[piece];
    }

    /**
     * Checks if the piece is a minor piece (bishop, knight).
     *
     * @param piece
     * @return true if the piece is a minor piece, false otherwise
     * @throws IllegalArgumentException if the piece is invalid
     */
    protected static boolean isPieceMinor(int piece) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("Invalid piece");
        return isPieceMinor[piece];
    }

    /**
     * Converts a file and rank value to a square value on the 120
     * square board.
     *
     * @param file
     * @param rank
     * @throws IllegalArgumentException if the file is invalid
     * @throws IllegalArgumentException if the rank is invalid
     */
    protected static int convertFileRankToSqr(int file, int rank) {
        if (file < BoardFile.FILE_A.value ||
            file > BoardFile.FILE_NONE.value)
            throw new IllegalArgumentException("Invalid file");
        if (rank < BoardRank.RANK_1.value ||
            rank > BoardRank.RANK_NONE.value)
            throw new IllegalArgumentException("Invalid rank");
        return 21 + file + 10 * rank;
    }

    /**
     * Get corresponding piece char.
     *
     * @param piece
     * @return corresponding piece char
     * @throws IllegalArgumentException if the piece is invalid
     */
    protected static char getPieceChar(int piece) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("Invalid piece");
        return pieceChars.charAt(piece);
    }

    /**
     * Get corresponding side char.
     *
     * @param side
     * @return corresponding side char
     * @throws IllegalArgumentException if the side is invalid
     */
    protected static char getSideChar(int side) {
        if (side < BoardColor.WHITE.value ||
            side > BoardColor.BOTH.value)
            throw new IllegalArgumentException("Invalid side");
        return sideChars.charAt(side);
    }

    /**
     * Get corresponding file char.
     *
     * @param file
     * @return corresponding file char
     * @throws IllegalArgumentException if the file is invalid
     */
    protected static char getFileChar(int file) {
        if (file < BoardFile.FILE_A.value ||
            file > BoardFile.FILE_NONE.value)
            throw new IllegalArgumentException("Invalid file");
        return fileChars.charAt(file);
    }

    /**
     * Get corresponding rank char.
     *
     * @param rank
     * @return corresponding rank char
     * @throws IllegalArgumentException if the rank is invalid
     */
    protected static char getRankChar(int rank) {
        if (rank < BoardRank.RANK_1.value ||
            rank > BoardRank.RANK_NONE.value)
            throw new IllegalArgumentException("Invalid rank");
        return rankChars.charAt(rank);
    }

    /**
     * Get corresponding piece value.
     *
     * @param piece
     * @return corresponding piece value
     * @throws IllegalArgumentException if the piece is invalid
     */
    protected static int getPieceValue(int piece) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("Invalid piece");
        return pieceValue[piece];
    }

    /**
     * Get corresponding piece color.
     *
     * @param piece
     * @return corresponding piece color
     * @throws IllegalArgumentException if the piece is invalid
     */
    protected static int getPieceColor(int piece) {
        if (piece < BoardPiece.EMPTY.value ||
            piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("Invalid piece");
        return pieceColor[piece];
    }

    /**
     * Get corresponding castle permission.
     *
     * @param square
     * @return corresponding castle permission
     */
    protected static int getCastlePerm(int square) {
        return castlePerm[square];
    }

    /**
     * Checks if the square is on the board.
     *
     * @param boardStructure
     * @param square
     * @return true if the square is on board, false otherwise
     */
    protected static boolean isSquareOnBoard(BoardStructure boardStructure,
                                      int square) {
        return boardStructure.fileBoard[square] != BoardSquare.OFFBOARD.value;
    }

    /**
     * Checks if the square is off the board.
     *
     * @param boardStructure
     * @param square
     * @return true if the square is off board, false otherwise
     */
    protected static boolean isSquareOffBoard(BoardStructure boardStructure,
                                         int square) {
        return boardStructure.fileBoard[square] == BoardSquare.OFFBOARD.value;
    }

    /**
     * Checks if the side is valid.
     *
     * @param side
     * @return true if the side is valid, false otherwise
     */
    protected static boolean isSideValid(int side) {
        return side == BoardColor.WHITE.value || side == BoardColor.BLACK.value;
    }

    /**
     * Checks if the file is valid.
     *
     * @param file
     * @return true if the file is valid, false otherwise
     */
    protected static boolean isFileValid(int file) {
        return file >= 0 && file <= 7;
    }

    /**
     * Checks if the rank is valid.
     *
     * @param rank
     * @return true if the rank is valid, false otherwise
     */
    protected static boolean isRankValid(int rank) {
        return rank >= 0 && rank <= 7;
    }

    /**
     * Checks if the piece is valid or the empty piece.
     *
     * @param piece
     * @return true if the piece is valid or the empty piece, false otherwise
     */
    protected static boolean isPieceValidOrEmpty(int piece) {
        return piece >= BoardPiece.EMPTY.value &&
               piece <= BoardPiece.BLACK_KING.value;
    }

    /**
     * Checks if the piece is valid (but not the empty piece).
     *
     * @param piece
     * @return true if the piece is valid, false otherwise
     */
    protected static boolean isPieceValid(int piece) {
        return piece >= BoardPiece.WHITE_PAWN.value &&
               piece >= BoardPiece.BLACK_KING.value;
    }

}
