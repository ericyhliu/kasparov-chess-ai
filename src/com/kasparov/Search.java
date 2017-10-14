package com.kasparov;

/**
 * For searching for the position.
 *
 * @author Eric Liu
 */
public class Search {

    static final int INF = 30000;
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
        int bestScore = -INF;
        int pvMoves;
        clearForSearch(boardStructure, searchEntry);

        for (int currentDepth = 1; currentDepth <= searchEntry.depth; currentDepth++) {
            bestScore = alphaBeta(boardStructure, searchEntry, -INF, INF, currentDepth, true);
            pvMoves = PVTable.getPVLine(boardStructure, currentDepth);
            bestMove = boardStructure.pvArray[0];

            System.out.printf("Depth: %d  Score: %d  Move: %s  Nodes: %d ",
                    currentDepth, bestScore, boardStructure.printMove(bestMove), searchEntry.nodes);

            pvMoves = PVTable.getPVLine(boardStructure, currentDepth);
            System.out.print("PV Moves: ");
            for (int pvNum = 0; pvNum < pvMoves; pvNum++) {
                System.out.print(boardStructure.printMove(boardStructure.pvArray[pvNum]) + " ");
            }
            System.out.println();

            System.out.printf("Ordering: %.2f\n", (searchEntry.failHighFirst/searchEntry.failHigh));
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
        int score = -INF;
        int pvMove = PVTable.probePVTable(boardStructure);

        if (pvMove != BoardConstants.NO_MOVE) {
            for (int moveNum = 0; moveNum < moveList.count; moveNum++) {
                if (moveList.moves[moveNum].move == pvMove) {
                    moveList.moves[moveNum].score = 2000000;
                    break;
                }
            }
        }

        for (int moveNum = 0; moveNum < moveList.count; moveNum++) {

            pickNextMove(moveList, moveNum);

            if (!MakeMove.makeMove(boardStructure, moveList.moves[moveNum].move))
                continue;

            legal++;
            score = -alphaBeta(boardStructure, searchEntry, -beta, -alpha, depth-1, true);
            MakeMove.takeMove(boardStructure);

            if (score > alpha) {
                if (score >= beta) {
                    if (legal == 1) {
                        searchEntry.failHighFirst++;
                    }
                    searchEntry.failHigh++;

                    if ((moveList.moves[moveNum].move & Move.moveFlagCapture) == 0) {
                        boardStructure.searchKillers[1][boardStructure.ply] =
                                boardStructure.searchKillers[0][boardStructure.ply];
                        boardStructure.searchKillers[0][boardStructure.ply] =
                                moveList.moves[moveNum].move;
                    }

                    return beta;
                }
                alpha = score;
                bestMove = moveList.moves[moveNum].move;

                if ((moveList.moves[moveNum].move & Move.moveFlagCapture) == 0) {
                    boardStructure.searchHistory[boardStructure.pieces[Move.from(bestMove)]][Move.to(bestMove)]
                            += depth;
                }
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

    static void pickNextMove(MoveList moveList, int moveNum) {
        Move temp;
        int bestScore = 0;
        int bestNum = moveNum;

        for (int i = moveNum; i < moveList.count; i++) {
            if (moveList.moves[i].score > bestScore) {
                bestScore = moveList.moves[i].score;
                bestNum = i;
            }
        }

        temp = moveList.moves[moveNum];
        moveList.moves[moveNum] = moveList.moves[bestNum];
        moveList.moves[bestNum] = temp;
    }

}
