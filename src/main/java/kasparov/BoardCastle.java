package kasparov;

/**
 * Definitions for board castle links.
 *
 * @author Eric Liu
 */
public enum BoardCastle {

    WHITE_KING_CASTLE(1),
    WHITE_QUEEN_CASTLE(2),
    BLACK_KING_CASTLE(4),
    BLACK_QUEEN_CASTLE(8);

    protected final int value;

    /**
     * Constructor for BoardCastle enum.
     *
     * @param value
     */
    BoardCastle(int value) {
        this.value = value;
    }

}
