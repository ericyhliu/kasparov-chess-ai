package ca.ericliu.kasparov;

/**
 * Definitions for board colors.
 *
 * @author Eric Liu
 */
public enum BoardColor {

    WHITE(0),
    BLACK(1),
    BOTH(2);

    public final int value;

    BoardColor(int value) {
        this.value = value;
    }
}
