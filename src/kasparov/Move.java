package kasparov;

/**
 * Move structure.
 *
 * @author Eric Liu
 */
public class Move {

    private int move;
    private int score;

    /**
     * Initializes an empty Move.
     */
    protected Move() {
        move = 0;
        score = 0;
    }

    /**
     * Initializes a Move with a move and score.
     *
     * @param move
     * @param score
     */
    protected Move(int move, int score) {
        this.move = move;
        this.score = score;
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
     * Getter for score.
     *
     * @return score
     */
    protected int getScore() {
        return score;
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
     * Setter for score.
     *
     * @param score
     */
    protected void setScore(int score) {
        this.score = score;
    }

}
