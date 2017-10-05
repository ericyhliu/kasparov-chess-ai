package com.kasparov;

/**
 * List of moves.
 */
public class MoveList {

    Move[] moves;
    int count;

    public MoveList() {
        moves = new Move[BoardConstants.MAX_POSITION_MOVES];
        count = 0;
    }

    public void addMove(int move) {
        moves[count] = new Move(move, 0);
        count++;
    }

    public Move getMove(int i) {
        return moves[i];
    }

    public Move[] getMoves() {
        return moves;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int i) {
        count = 0;
    }
}
