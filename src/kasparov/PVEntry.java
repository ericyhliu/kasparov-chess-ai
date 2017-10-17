package kasparov;

/**
 * Entry into the principal variation table.
 *
 * @author Eric Liu
 */
public class PVEntry {

    private long posKey;
    private int move;

    /**
     * Initializes an empty PVEntry.
     */
    protected PVEntry() {
        posKey = -1;
        move = -1;
    }

    /**
     * Initializes a PVEntry with a position key and move.
     */
    protected PVEntry(long posKey, int move) {
        this.posKey = posKey;
        this.move = move;
    }

    /**
     * Getter for position key.
     *
     * @return posKey
     */
    public long getPosKey() {
        return posKey;
    }

    /**
     * Getter for move.
     *
     * @return move
     */
    public int getMove() {
        return move;
    }

    /**
     * Setter for position key.
     *
     * @param posKey
     */
    public void setPosKey(long posKey) {
        this.posKey = posKey;
    }

    /**
     * Setter for move.
     *
     * @param move
     */
    public void setMove(int move) {
        this.move = move;
    }
}
