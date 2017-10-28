package kasparov;

/**
 * Evaluates positions.
 *
 * @author Eric Liu
 */
public class PositionEvaluator {

    /**
     * Pawn position score table.
     */
    private static final int[] pawnTable = {
         0,   0,   0,   0,   0,   0,   0,   0,
        10,  10,   0, -10, -10,   0,  10,  10,
         5,   0,   0,   5,   5,   0,   0,   5,
         0,   0,  10,  20,  20,  10,   0,   0,
         5,   5,   5,  10,  10,   5,   5,   5,
        10,  10,  10,  20,  20,  10,  10,  10,
        20,  20,  20,  30,  30,  20,  20,  20,
         0,   0,   0,   0,   0,   0,   0,   0
    };

    /**
     * Knight position score table.
     */
    private static final int[] knightTable = {
         0,	-10,   0,   0,   0,   0, -10,   0,
         0,	  0,   0,   5,   5,   0,   0,   0,
         0,	  0,  10,  10,  10,  10,   0,   0,
         0,	  0,  10,  20,  20,  10,   5,   0,
         5,	 10,  15,  20,  20,  15,  10,   5,
         5,	 10,  10,  20,  20,  10,  10,   5,
         0,	  0,   5,  10,  10,   5,   0,   0,
        0 ,	  0,   0,   0,   0,   0,   0,   0
    };

    /**
     * Bishop position score table.
     */
    private static final int[] bishopTable = {
         0,	  0, -10,   0,	 0,	-10,   0,   0,
         0,	  0,   0,  10,  10,	  0,   0,   0,
         0,	  0,  10,  15,  15,	 10,   0,   0,
         0,	 10,  15,  20,  20,	 15,  10,   0,
         0,	 10,  15,  20,  20,	 15,  10,   0,
         0,	  0,  10,  15,  15,	 10,   0,   0,
         0,	  0,   0,  10,  10,	  0,   0,   0,
         0,	  0,   0,   0,   0,	  0,   0,   0
    };

    /**
     * Rook position score table.
     */
    private static final int[] rookTable = {
         0,	  0,   5,  10,  10,	  5,   0,   0,
         0,	  0,   5,  10,  10,	  5,   0,	0,
         0,	  0,   5,  10,  10,	  5,   0,	0,
         0,	  0,   5,  10,  10,	  5,   0,	0,
         0,	  0,   5,  10,  10,	  5,   0,	0,
         0,	  0,   5,  10,  10,	  5,   0,	0,
        25,	 25,  25,  25,  25,  25,  25,  25,
         0,	  0,   5,  10,  10,	  5,   0,	0
    };

    /**
     * Mirrored 64-square positions.
     */
    private static final int[] mirror64 = {
        56,	 57,  58,  59,	60,	 61,  62,  63,
        48,	 49,  50,  51,	52,	 53,  54,  55,
        40,	 41,  42,  43,	44,	 45,  46,  47,
        32,	 33,  34,  35,	36,	 37,  38,  39,
        24,	 25,  26,  27,	28,	 29,  30,  31,
        16,	 17,  18,  19,	20,	 21,  22,  23,
         8,	  9,  10,  11,	12,	 13,  14,  15,
         0,	  1,   2,	3,	 4,	  5,   6,	7
    };


    /**
     * Get pawn score.
     *
     * @param i
     * @return pawn score
     */
    protected static int getPawnScore(int i) {
        return pawnTable[i];
    }

    /**
     * Get knight score.
     *
     * @param i
     * @return knight score
     */
    protected static int getKnightScore(int i) {
        return knightTable[i];
    }

    /**
     * Get bishop score.
     *
     * @param i
     * @return bishop score
     */
    protected static int getBishopScore(int i) {
        return bishopTable[i];
    }

    /**
     * Get rook score.
     *
     * @param i
     * @return rook score
     */
    protected static int getRookScore(int i) {
        return rookTable[i];
    }

    /**
     * Get mirrored 64-square position.
     *
     * @param i
     * @return mirrored 64-square position
     */
    protected static int getMirror64Position(int i) {
        return mirror64[i];
    }

    /**
     * Evaluate position.
     *
     * @param boardStructure
     * @return position score
     */
    protected static int evaluatePosition(BoardStructure boardStructure) {
        int piece, pieceNum, square;
        int score = boardStructure.getMaterial(BoardColor.WHITE.value) -
                boardStructure.getMaterial(BoardColor.BLACK.value);

        // Pawns:
        piece = BoardPiece.WHITE_PAWN.value;
        for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(piece); pieceNum++) {
            square = boardStructure.getPieceListEntry(piece, pieceNum);
            score += getPawnScore(boardStructure.sqr64(square));
        }

        piece = BoardPiece.BLACK_PAWN.value;
        for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(piece); pieceNum++) {
            square = boardStructure.getPieceListEntry(piece, pieceNum);
            score -= getPawnScore(getMirror64Position(boardStructure.sqr64(square)));
        }

        // Knights:
        piece = BoardPiece.WHITE_KNIGHT.value;
        for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(piece); pieceNum++) {
            square = boardStructure.getPieceListEntry(piece, pieceNum);
            score += getKnightScore(boardStructure.sqr64(square));
        }

        piece = BoardPiece.BLACK_KNIGHT.value;
        for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(piece); pieceNum++) {
            square = boardStructure.getPieceListEntry(piece, pieceNum);
            score -= getKnightScore(getMirror64Position(boardStructure.sqr64(square)));
        }

        // Bishops:
        piece = BoardPiece.WHITE_BISHOP.value;
        for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(piece); pieceNum++) {
            square = boardStructure.getPieceListEntry(piece, pieceNum);
            score += getBishopScore(boardStructure.sqr64(square));
        }

        piece = BoardPiece.BLACK_BISHOP.value;
        for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(piece); pieceNum++) {
            square = boardStructure.getPieceListEntry(piece, pieceNum);
            score -= getBishopScore(getMirror64Position(boardStructure.sqr64(square)));
        }

        // Rooks:
        piece = BoardPiece.WHITE_ROOK.value;
        for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(piece); pieceNum++) {
            square = boardStructure.getPieceListEntry(piece, pieceNum);
            score += getRookScore(boardStructure.sqr64(square));
        }

        piece = BoardPiece.BLACK_ROOK.value;
        for (pieceNum = 0; pieceNum < boardStructure.getPieceNum(piece); pieceNum++) {
            square = boardStructure.getPieceListEntry(piece, pieceNum);
            score -= getRookScore(getMirror64Position(boardStructure.sqr64(square)));
        }

        if (boardStructure.getSide() == BoardColor.WHITE.value)
            return score;
        return -score;
    }
}
