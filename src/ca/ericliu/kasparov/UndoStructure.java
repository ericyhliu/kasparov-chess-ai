package ca.ericliu.kasparov;

/**
 * Structure used for undoing board positions.
 *
 * @author Eric Liu
 */
public class UndoStructure {

    /**
     * Status of the current move.
     */
    int move;

    /**
     * Status of the active en passant square.
     */
    int enPassant;

    /**
     * Status of the 50-move rule counter.
     */
    int fiftyMove;

    /**
     * Unique 64 bit key for the current position.
     */
    long posKey;

    /**
     * Status of the castle permissions.
     */
    int castlePerm;

    /**
     * Initializes an empty UndoStructure.
     */
    public UndoStructure() {}
}
