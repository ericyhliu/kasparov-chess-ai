package kasparov;

/**
 * Definitions for board ranks.
 *
 * @author Eric Liu
 */
public enum BoardRank {

    RANK_1(0),
    RANK_2(1),
    RANK_3(2),
    RANK_4(3),
    RANK_5(4),
    RANK_6(5),
    RANK_7(6),
    RANK_8(7),
    RANK_NONE(8);

    protected final int value;

    BoardRank(int value) {
        this.value = value;
    }

}
