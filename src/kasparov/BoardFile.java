package kasparov;

/**
 * Definitions for the the board files.
 *
 * @author Eric Liu
 */
public enum BoardFile {

    FILE_A(0),
    FILE_B(1),
    FILE_C(2),
    FILE_D(3),
    FILE_E(4),
    FILE_F(5),
    FILE_G(6),
    FILE_H(7),
    FILE_NONE(8);

    protected final int value;

    /**
     * Constructor for BoardFile enum.
     *
     * @param value
     */
    BoardFile(int value) {
        this.value = value;
    }

}
