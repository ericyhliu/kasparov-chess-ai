package kasparov;

/**
 * For perft testing (performance testing).
 *
 * @author Eric Liu
 */
public class PerftTest {

    long leafNodes;

    /**
     * Initialize an empty PerftTest.
     */
    public PerftTest() {}

    /**
     * Run peft test.
     *
     * @param boardStructure
     * @param depth
     */
    void perft(BoardStructure boardStructure, int depth) {
        if (depth == 0) {
            leafNodes++;
            return;
        }

        MoveList moveList = new MoveList();
        MoveGenerator.generateAllMoves(boardStructure, moveList);

        for (int moveNum = 0; moveNum < moveList.count; moveNum++) {
            if (!MakeMove.makeMove(boardStructure, moveList.moves[moveNum].getMove()))
                continue;
            perft(boardStructure, depth-1);
            MakeMove.takeMove(boardStructure);
        }
    }

    /**
     * Start perft test.
     *
     * @param boardStructure
     * @param depth
     * @return
     */
    long perftTest(BoardStructure boardStructure, int depth) {
        System.out.println("\nStarting Test to Depth: " + depth);
        leafNodes = 0;
        long start = Time.getTimeInMilliseconds();
        MoveList moveList = new MoveList();
        MoveGenerator.generateAllMoves(boardStructure, moveList);

        int move;
        for (int moveNum = 0; moveNum < moveList.count; moveNum++) {
            move = moveList.moves[moveNum].getMove();
            if (!MakeMove.makeMove(boardStructure, move))
                continue;
            long cumNodes = leafNodes;
            perft(boardStructure, depth - 1);
            MakeMove.takeMove(boardStructure);
            long oldNodes = leafNodes - cumNodes;
             System.out.println("Move " + (moveNum+1) + ": " +
                     boardStructure.printMove(move) + ": " + oldNodes);
        }
        System.out.println("Total Nodes Visited: " + leafNodes);
        System.out.println("Test completed in: " + (Time.getTimeInMilliseconds() - start) + " ms");
        return leafNodes;
    }
}
