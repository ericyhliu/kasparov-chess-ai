package kasparov;

/**
 * For generating a position (hash) key.
 *
 * @author Eric Liu
 */
public class PositionKey {

    protected static long generatePositionKey(BoardStructure boardStructure) {
        long finalKey = 0;
        int piece;

        for (int sqr = 0; sqr < BoardConstants.BOARD_SQR_NUM; sqr++) {
            piece = boardStructure.pieces[sqr];

            if (piece != BoardSquare.OFFBOARD.value && piece != BoardPiece.EMPTY.value) {
                assert (piece >= BoardPiece.WHITE_PAWN.value &&
                        piece <= BoardPiece.BLACK_KING.value);
                finalKey ^= boardStructure.pieceKeys[piece][sqr];
            }
        }

        if (boardStructure.side == BoardColor.WHITE.value) {
            finalKey ^= boardStructure.sideKey;
        }

        if (boardStructure.enPassant != BoardSquare.NONE.value) {
            assert(boardStructure.enPassant >= 0 &&
                   boardStructure.enPassant < BoardConstants.BOARD_SQR_NUM);
            finalKey ^= boardStructure.pieceKeys[BoardPiece.EMPTY.value][boardStructure.enPassant];
        }

        assert(boardStructure.castlePerm >= 0 && boardStructure.castlePerm <= 15);

        finalKey ^= boardStructure.castleKeys[boardStructure.castlePerm];

        System.out.println();

        return finalKey;
    }

}
