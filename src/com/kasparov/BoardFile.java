package com.kasparov;

/**
 * Definitions for the the board files.
 *
 * @author Eric Liu
 */
public enum BoardFile {

    FILE_NONE(0),
    FILE_A(1),
    FILE_B(2),
    FILE_C(3),
    FILE_D(4),
    FILE_E(5),
    FILE_F(6),
    FILE_G(7),
    FILE_H(8);

    public final int value;

    BoardFile(int value) {
        this.value = value;
    }

}
