package kasparov;

/**
 * Structure used for undoing board positions.
 *
 * @author Eric Liu
 */
public class UndoStructure {

    private int move;
    private int enPassant;
    private int fiftyMove;
    private int castlePerm;
    private long posKey;

    /**
     * Initializes an empty UndoStructure.
     */
    protected UndoStructure() {}

    /**
     * Initializes an UndoStructure with move, enPassant, fiftyMove,
     * castlePerm, posKey.
     *
     * @param move
     * @param enPassant
     * @param fiftyMove
     * @param castlePerm
     * @param posKey
     */
    protected UndoStructure(int move, int enPassant, int fiftyMove,
                            int castlePerm, long posKey) {
        this.move = move;
        this.enPassant = enPassant;
        this.fiftyMove = fiftyMove;
        this.castlePerm = castlePerm;
        this.posKey = posKey;
    }

    /**
     * Getter for move.
     *
     * @return move
     */
    protected int getMove() {
        return move;
    }

    /**
     * Getter for en passant.
     *
     * @return enPassant
     */
    protected int getEnPassant() {
        return enPassant;
    }

    /**
     * Getter for fifty move.
     *
     * @return fiftyMove
     */
    protected int getFiftyMove() {
        return fiftyMove;
    }

    /**
     * Getter for castle permissions.
     *
     * @return castlePerm
     */
    protected int getCastlePerm() {
        return castlePerm;
    }

    /**
     * Getter for position key.
     *
     * @return posKey
     */
    protected long getPosKey() {
        return posKey;
    }

    /**
     * Setter for move.
     *
     * @param move
     */
    protected void setMove(int move) {
        this.move = move;
    }

    /**
     * Setter for en passant.
     *
     * @param enPassant
     */
    protected void setEnPassant(int enPassant) {
        this.enPassant = enPassant;
    }

    /**
     * Setter for fifty move.
     *
     * @param fiftyMove
     */
    protected void setFiftyMove(int fiftyMove) {
        this.fiftyMove = fiftyMove;
    }

    /**
     * Setter for castle permissions.
     *
     * @param castlePerm
     */
    protected void setCastlePerm(int castlePerm) {
        this.castlePerm = castlePerm;
    }

    /**
     * Setter for position key.
     *
     * @param posKey
     */
    protected void setPosKey(long posKey) {
        this.posKey = posKey;
    }

}
