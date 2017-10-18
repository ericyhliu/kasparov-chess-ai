package kasparov;

/**
 * Definitions for board pieces.
 *
 * @author Eric Liu
 */
public enum BoardPiece {

    EMPTY(0),
    WHITE_PAWN(1),
    WHITE_KNIGHT(2),
    WHITE_BISHOP(3),
    WHITE_ROOK(4),
    WHITE_QUEEN(5),
    WHITE_KING(6),
    BLACK_PAWN(7),
    BLACK_KNIGHT(8),
    BLACK_BISHOP(9),
    BLACK_ROOK(10),
    BLACK_QUEEN(11),
    BLACK_KING(12);

    protected final int value;

    /**
     * Constructor for BoardPiece enum.
     *
     * @param value
     */
    BoardPiece(int value) {
        this.value = value;
    }
}
