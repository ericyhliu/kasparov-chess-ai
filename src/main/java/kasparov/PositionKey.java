package kasparov;

/**
 * For generating a position (hash) key.
 *
 * @author Eric Liu
 */
public class PositionKey {

    /**
     * Generates position key.
     *
     * @param boardStructure
     * @return position key
     */
    protected static long generatePositionKey(BoardStructure boardStructure) {
        long finalKey = 0;
        int piece;

        for (int sqr = 0; sqr < BoardConstants.BOARD_SQR_NUM; sqr++) {
            piece = boardStructure.getPiece(sqr);

            if (piece != BoardSquare.OFFBOARD.value &&
                piece != BoardPiece.EMPTY.value) {
                finalKey ^= boardStructure.getPieceKey(piece, sqr);
            }
        }

        if (boardStructure.getSide() == BoardColor.WHITE.value) {
            finalKey ^= boardStructure.getSideKey();
        }

        if (boardStructure.getEnPassant() != BoardSquare.NONE.value) {
            finalKey ^= boardStructure.getPieceKey(BoardPiece.EMPTY.value,
                    boardStructure.getEnPassant());
        }

        finalKey ^= boardStructure.getCastleKey(boardStructure.getCastlePerm());

        return finalKey;
    }

}
