package com.kasparov;

/**
 * For perft testing (performance testing).
 *
 * @author Eric Liu
 */
public class PerftTest {

    long leafNodes;

    public PerftTest() {}

    void perft(BoardStructure boardStructure, int depth) {
        if (depth == 0) {
            leafNodes++;
            return;
        }

        MoveList moveList = new MoveList();
        MoveGenerator.generateAllMoves(boardStructure, moveList);

        for (int moveNum = 0; moveNum < moveList.count; moveNum++) {
            if (!MakeMove.makeMove(boardStructure, moveList.moves[moveNum].move))
                continue;
            perft(boardStructure, depth-1);
            MakeMove.takeMove(boardStructure);
        }
    }

    void perftTest(BoardStructure boardStructure, int depth) {
        System.out.println("\nStarting Test to Depth: " + depth);
        leafNodes = 0;

        MoveList moveList = new MoveList();
        MoveGenerator.generateAllMoves(boardStructure, moveList);

        int move;
        for (int moveNum = 0; moveNum < moveList.count; moveNum++) {
            move = moveList.moves[moveNum].move;
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
    }
}
