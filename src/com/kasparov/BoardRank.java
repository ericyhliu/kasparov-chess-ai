package com.kasparov;

/**
 * Definitions for board ranks.
 *
 * @author Eric Liu
 */
public enum BoardRank {

    RANK_NONE(0),
    RANK_1(1),
    RANK_2(2),
    RANK_3(3),
    RANK_4(4),
    RANK_5(5),
    RANK_6(6),
    RANK_7(7),
    RANK_8(8);

    public final int value;

    BoardRank(int value) {
        this.value = value;
    }

}
