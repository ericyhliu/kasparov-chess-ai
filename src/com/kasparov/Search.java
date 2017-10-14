package com.kasparov;

/**
 * For searching for the position.
 *
 * @author Eric Liu
 */
public class Search {

    static final int MATE = 29000;

    static boolean isRepetition(BoardStructure boardStructure) {
        for (int i = boardStructure.historyPly - boardStructure.fiftyMove;
             i < boardStructure.historyPly - 1; i++) {
            if (boardStructure.positionKey == boardStructure.history[i].posKey)
                return true;
        }
        return false;
    }

    static void searchPosition(BoardStructure boardStructure, SearchEntry searchEntry) {
        int bestMove = BoardConstants.NO_MOVE;
        int bestScore = Integer.MIN_VALUE;
        int pvMoves = 0;
        clearForSearch(boardStructure, searchEntry);

        for (int currentDepth = 1; currentDepth <= searchEntry.depth; currentDepth++) {
            bestScore = alphaBeta(boardStructure, searchEntry,
                    Integer.MIN_VALUE, Integer.MAX_VALUE, currentDepth, true);

            // check for time here

            pvMoves = PVTable.getPVLine(boardStructure, currentDepth);
            bestMove = boardStructure.pvArray[0];
            System.out.println("Depth: " + currentDepth);
            System.out.println("Best Score: " + bestScore);
            System.out.println("Move: " + boardStructure.printMove(bestMove));
            System.out.println("Nodes: " + searchEntry.nodes);
            System.out.print("PV Moves: ");
            for (int pvNum = 0; pvNum < pvMoves; pvNum++) {
                System.out.print(boardStructure.printMove(boardStructure.pvArray[pvNum]) + " ");
            }
            System.out.println();
        }
    }

    static void clearForSearch(BoardStructure boardStructure, SearchEntry searchEntry) {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < BoardConstants.BOARD_SQR_NUM; j++)
                boardStructure.searchHistory[i][j] = 0;
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < BoardConstants.MAX_DEPTH; j++)
                boardStructure.searchKillers[i][j] = 0;
        }

        boardStructure.pvTable.clearPVTable();
        boardStructure.ply = 0;

        searchEntry.startTime = Time.getTimeInMilleseconds();
        searchEntry.stopped = 0;
        searchEntry.nodes = 0;
    }

    static void checkUp() {
        // check if time up or interrupt from GUI
    }

    static int quiescenceSearch(BoardStructure boardStructure, SearchEntry searchEntry,
                                int alpha, int beta) {
        return 0;
    }

    static int alphaBeta(BoardStructure boardStructure, SearchEntry searchEntry,
                         int alpha, int beta, int depth, boolean isNull) {
        if (depth == 0) {
            searchEntry.nodes++;
            return PositionEvaluator.evaluatePosition(boardStructure);
        }

        searchEntry.nodes++;

        if (isRepetition(boardStructure) || boardStructure.fiftyMove >= 100)
            return 0;

        if (boardStructure.ply > BoardConstants.MAX_DEPTH - 1)
            return PositionEvaluator.evaluatePosition(boardStructure);

        MoveList moveList = new MoveList();
        MoveGenerator.generateAllMoves(boardStructure, moveList);
        int legal = 0;
        int oldAlpha = alpha;
        int bestMove = BoardConstants.NO_MOVE;
        int score = Integer.MIN_VALUE;

        for (int moveNum = 0; moveNum < moveList.count; moveNum++) {
            if (!MakeMove.makeMove(boardStructure, moveList.moves[moveNum].move))
                continue;

            legal++;
            score = -alphaBeta(boardStructure, searchEntry, -beta, -alpha, depth-1, true);
            MakeMove.takeMove(boardStructure);

            if (score > alpha) {
                if (score >= beta)
                    return beta;
                alpha = score;
                bestMove = moveList.moves[moveNum].move;
            }
        }

        if (legal == 0) {
            if (SquareAttacked.squareAttacked(boardStructure.kingSqr[boardStructure.side],
                    boardStructure.side ^ 1, boardStructure)) {
                return -MATE + boardStructure.ply;
            } else {
                return 0;
            }
        }

        if (alpha != oldAlpha) {
            PVTable.storePVMove(boardStructure, bestMove);
        }

        return alpha;
    }
}
