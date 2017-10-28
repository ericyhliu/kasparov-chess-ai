package kasparov;

/**
 * Entry into the principal variation table.
 *
 * @author Eric Liu
 */
public class PVEntry {

    private long positionKey;
    private int move;


    /**
     * Initializes an empty PVEntry.
     */
    protected PVEntry() {
        positionKey = -1;
        move = -1;
    }

    /**
     * Initializes a PVEntry with a position key and move.
     */
    protected PVEntry(long positionKey, int move) {
        this.positionKey = positionKey;
        this.move = move;
    }

    /**
     * Getter for position key.
     *
     * @return positionKey
     */
    public long getPosKey() {
        return positionKey;
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
     * @param positionKey
     */
    public void setPosKey(long positionKey) {
        this.positionKey = positionKey;
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
