package kasparov;

/**
 * Definitions for board colors.
 *
 * @author Eric Liu
 */
public enum BoardColor {

    WHITE(0),
    BLACK(1),
    BOTH(2);

    protected final int value;

    /**
     * Constructor for BoardColor enum.
     *
     * @param value
     */
    BoardColor(int value) {
        this.value = value;
    }

}
