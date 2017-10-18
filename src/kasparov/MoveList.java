package kasparov;

/**
 * List of moves.
 */
public class MoveList {

    Move[] moves;
    int count;

    /**
     * Initializes a MoveList.
     */
    protected MoveList() {
        moves = new Move[BoardConstants.MAX_POSITION_MOVES];
        count = 0;
    }

    /**
     * Getter for moves.
     *
     * @return moves
     */
    protected Move[] getMoves() {
        return moves;
    }

    /**
     * Getter for count.
     *
     * @return
     */
    protected int getCount() {
        return count;
    }

    /**
     * Setter for moves.
     *
     * @param moves
     */
    protected void setMoves(Move[] moves) {
        this.moves = moves;
    }

    /**
     * Setter for count.
     *
     * @param count
     */
    protected void setCount(int count) {
        this.count = count;
    }


    protected void addMove(int move) {
        moves[count] = new Move(move, 0);
        count++;
    }

    protected Move getMove(int i) {
        return moves[i];
    }

    protected static void printMoveList(MoveList moveList, BoardStructure boardStructure) {
        int move = 0;
        int score = 0;
        System.out.println("Move List: " + moveList.count);
        for (int i = 0; i < moveList.getCount(); i++) {
            move = moveList.moves[i].getMove();
            score = moveList.moves[i].getScore();
            System.out.println("Move: " + boardStructure.printMove(move) + "   Score: " + score);
        }

        System.out.println("Move List Total Moves: " + moveList.count);
    }
}
