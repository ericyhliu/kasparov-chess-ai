package kasparov;

/**
 * Position evaluator.
 *
 * @author Eric Liu
 */
public class PositionEvaluator {

    protected static final int[] pawnTable = {
         0,   0,   0,   0,   0,   0,   0,   0,
        10,  10,   0, -10, -10,   0,  10,  10,
         5,   0,   0,   5,   5,   0,   0,   5,
         0,   0,  10,  20,  20,  10,   0,   0,
         5,   5,   5,  10,  10,   5,   5,   5,
        10,  10,  10,  20,  20,  10,  10,  10,
        20,  20,  20,  30,  30,  20,  20,  20,
         0,   0,   0,   0,   0,   0,   0,   0
    };

    protected static final int[] knightTable = {
         0,	-10,   0,   0,   0,   0, -10,   0,
         0,	  0,   0,   5,   5,   0,   0,   0,
         0,	  0,  10,  10,  10,  10,   0,   0,
         0,	  0,  10,  20,  20,  10,   5,   0,
         5,	 10,  15,  20,  20,  15,  10,   5,
         5,	 10,  10,  20,  20,  10,  10,   5,
         0,	  0,   5,  10,  10,   5,   0,   0,
        0 ,	  0,   0,   0,   0,   0,   0,   0
    };

    protected static final int[] bishopTable = {
         0,	  0, -10,   0,	 0,	-10,   0,   0,
         0,	  0,   0,  10,  10,	  0,   0,   0,
         0,	  0,  10,  15,  15,	 10,   0,   0,
         0,	 10,  15,  20,  20,	 15,  10,   0,
         0,	 10,  15,  20,  20,	 15,  10,   0,
         0,	  0,  10,  15,  15,	 10,   0,   0,
         0,	  0,   0,  10,  10,	  0,   0,   0,
         0,	  0,   0,   0,   0,	  0,   0,   0
    };

    protected static final int[] rookTable = {
         0,	  0,   5,  10,  10,	  5,   0,   0,
         0,	  0,   5,  10,  10,	  5,   0,	0,
         0,	  0,   5,  10,  10,	  5,   0,	0,
         0,	  0,   5,  10,  10,	  5,   0,	0,
         0,	  0,   5,  10,  10,	  5,   0,	0,
         0,	  0,   5,  10,  10,	  5,   0,	0,
        25,	 25,  25,  25,  25,  25,  25,  25,
         0,	  0,   5,  10,  10,	  5,   0,	0
    };

    protected static final int[] mirror64 = {
        56,	 57,  58,  59,	60,	 61,  62,  63,
        48,	 49,  50,  51,	52,	 53,  54,  55,
        40,	 41,  42,  43,	44,	 45,  46,  47,
        32,	 33,  34,  35,	36,	 37,  38,  39,
        24,	 25,  26,  27,	28,	 29,  30,  31,
        16,	 17,  18,  19,	20,	 21,  22,  23,
         8,	  9,  10,  11,	12,	 13,  14,  15,
         0,	  1,   2,	3,	 4,	  5,   6,	7
    };

    protected static int evaluatePosition(BoardStructure boardStructure) {
        int piece;
        int pieceNum;
        int sqr;
        int score = boardStructure.material[BoardColor.WHITE.value] -
                boardStructure.material[BoardColor.BLACK.value];

        // Pawns:
        piece = BoardPiece.WHITE_PAWN.value;
        for (pieceNum = 0; pieceNum < boardStructure.pieceNum[piece]; pieceNum++) {
            sqr = boardStructure.pieceList[piece][pieceNum];
            score += pawnTable[boardStructure.sqr64(sqr)];
        }

        piece = BoardPiece.BLACK_PAWN.value;
        for (pieceNum = 0; pieceNum < boardStructure.pieceNum[piece]; pieceNum++) {
            sqr = boardStructure.pieceList[piece][pieceNum];
            score -= pawnTable[mirror64[boardStructure.sqr64(sqr)]];
        }

        // Knights:
        piece = BoardPiece.WHITE_KNIGHT.value;
        for (pieceNum = 0; pieceNum < boardStructure.pieceNum[piece]; pieceNum++) {
            sqr = boardStructure.pieceList[piece][pieceNum];
            score += knightTable[boardStructure.sqr64(sqr)];
        }

        piece = BoardPiece.BLACK_KNIGHT.value;
        for (pieceNum = 0; pieceNum < boardStructure.pieceNum[piece]; pieceNum++) {
            sqr = boardStructure.pieceList[piece][pieceNum];
            score -= knightTable[mirror64[boardStructure.sqr64(sqr)]];
        }

        // Bishops:
        piece = BoardPiece.WHITE_BISHOP.value;
        for (pieceNum = 0; pieceNum < boardStructure.pieceNum[piece]; pieceNum++) {
            sqr = boardStructure.pieceList[piece][pieceNum];
            score += bishopTable[boardStructure.sqr64(sqr)];
        }

        piece = BoardPiece.BLACK_BISHOP.value;
        for (pieceNum = 0; pieceNum < boardStructure.pieceNum[piece]; pieceNum++) {
            sqr = boardStructure.pieceList[piece][pieceNum];
            score -= bishopTable[mirror64[boardStructure.sqr64(sqr)]];
        }

        // Rooks:
        piece = BoardPiece.WHITE_ROOK.value;
        for (pieceNum = 0; pieceNum < boardStructure.pieceNum[piece]; pieceNum++) {
            sqr = boardStructure.pieceList[piece][pieceNum];
            score += rookTable[boardStructure.sqr64(sqr)];
        }

        piece = BoardPiece.BLACK_ROOK.value;
        for (pieceNum = 0; pieceNum < boardStructure.pieceNum[piece]; pieceNum++) {
            sqr = boardStructure.pieceList[piece][pieceNum];
            score -= rookTable[mirror64[boardStructure.sqr64(sqr)]];
        }

        if (boardStructure.side == BoardColor.WHITE.value) {
            return score;
        } else {
            return -score;
        }
    }
}
