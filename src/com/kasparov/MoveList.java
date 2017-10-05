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

    public static void printMoveList(MoveList moveList, BoardStructure boardStructure) {
        int move = 0;
        int score = 0;
        System.out.println("Move List: " + moveList.count);
        for (int i = 0; i < moveList.getCount(); i++) {
            move = moveList.moves[i].move;
            score = moveList.moves[i].score;
            System.out.println("Move: " + boardStructure.printMove(move) + "   Score: " + score);
        }

        System.out.println("Move List Total Moves: " + moveList.count);
    }
}
