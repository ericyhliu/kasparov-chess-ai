package com.kasparov;

/**
 * Definitions for board colors.
 *
 * @author Eric Liu
 */
public enum BoardColors {

    WHITE(0),
    BLACK(1),
    BOTH(2);

    public final int value;

    BoardColors(int value) {
        this.value = value;
    }
}
