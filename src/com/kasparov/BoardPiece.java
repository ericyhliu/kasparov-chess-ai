package com.kasparov;

/**
 * Definitions for board pieces.
 *
 * @author Eric Liu
 */
public enum BoardPiece {

    EMPTY(0),
    WHITE_PAWN(1),
    WHITE_BISHOP(2),
    WHITE_ROOK(3),
    WHITE_QUEEN(4),
    WHITE_KING(5),
    BLACK_PAWN(6),
    BLACK_BISHOP(7),
    BLACK_ROOK(8),
    BLACK_QUEEN(9),
    BLACK_KING(10),
    OFFBOARD(11);

    public final int value;

    BoardPiece(int value) {
        this.value = value;
    }
}
