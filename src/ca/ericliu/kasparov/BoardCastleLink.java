package ca.ericliu.kasparov;

/**
 * Definitions for board castle links.
 *
 * @author Eric Liu
 */
public enum BoardCastleLink {

    WHITE_KING_CASTLE(1),
    WHITE_QUEEN_CASTLE(2),
    BLACK_KING_CASTLE(4),
    BLACK_QUEEN_CASTLE(8);

    public final int value;

    BoardCastleLink(int value) {
        this.value = value;
    }

}
