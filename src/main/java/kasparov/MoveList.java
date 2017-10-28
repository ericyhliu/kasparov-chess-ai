package kasparov;

/**
 * List of moves.
 */
public class MoveList {

    private Move[] moves;
    private int count;

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

    /**
     * Add a move to the MoveList.
     *
     * @param move
     */
    protected void addMove(int move) {
        moves[count] = new Move(move, 0);
        count++;
    }

    /**
     * Return the Move at the specified index.
     *
     * @param i
     * @return Move at index i
     */
    protected Move getMove(int i) {
        return moves[i];
    }

    /**
     * Sets the Move at the specified index.
     *
     * @param move
     * @param i
     */
    protected void setMove(Move move, int i) {
        moves[i] = move;
    }

    /**
     * Print the MoveList.
     *
     * @param moveList
     * @param boardStructure
     */
    protected static void printMoveList(MoveList moveList,
                                        BoardStructure boardStructure) {
        int move = 0;
        int score = 0;
        System.out.println("Move List: " + moveList.count);
        for (int i = 0; i < moveList.getCount(); i++) {
            move = moveList.moves[i].getMove();
            score = moveList.moves[i].getScore();
            System.out.println("Move: " + boardStructure.printMove(move) +
                    "   Score: " + score);
        }

        System.out.println("Move List Total Moves: " + moveList.count);
    }
}
