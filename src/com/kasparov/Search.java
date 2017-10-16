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

            if (searchEntry.isStopped == true) {
                break;
            }

            pvMoves = PVTable.getPVLine(boardStructure, currentDepth);
            bestMove = boardStructure.pvArray[0];

            System.out.printf("info score cp %d depth %d nodes %d time %d ",
                    bestScore, currentDepth, searchEntry.nodes,
                    Time.getTimeInMilleseconds() - searchEntry.startTime);

            pvMoves = PVTable.getPVLine(boardStructure, currentDepth);
            System.out.print("pv ");
            for (int pvNum = 0; pvNum < pvMoves; pvNum++) {
                System.out.print(boardStructure.printMove(boardStructure.pvArray[pvNum]) + " ");
            }
            System.out.println();
            // System.out.printf("\nOrdering: %.2f\n", (searchEntry.failHighFirst/searchEntry.failHigh));
        }

        System.out.printf("bestmove %s\n", boardStructure.printMove(bestMove));
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
        searchEntry.isStopped = false;
        searchEntry.nodes = 0;
        searchEntry.failHigh = 0;
        searchEntry.failHighFirst = 0;
    }

    static void checkUp(SearchEntry searchEntry) {
        if (searchEntry.timeSet && Time.getTimeInMilleseconds() > searchEntry.stopTime) {
            searchEntry.isStopped = true;
        }
    }

    static int quiescenceSearch(BoardStructure boardStructure, SearchEntry searchEntry,
                                int alpha, int beta) {
        if ((searchEntry.nodes & 2047) == 0) {
            checkUp(searchEntry);
        }

        searchEntry.nodes++;

        if (isRepetition(boardStructure) || boardStructure.fiftyMove >= 100)
            return 0;

        if (boardStructure.ply > BoardConstants.MAX_DEPTH - 1)
            return PositionEvaluator.evaluatePosition(boardStructure);

        int score = PositionEvaluator.evaluatePosition(boardStructure);

        if (score >= beta) {
            return beta;
        }

        if (score >= alpha) {
            alpha = score;
        }

        MoveList moveList = new MoveList();
        MoveGenerator.generateAllCaptureMoves(boardStructure, moveList);

        int moveNum = 0;
        int legal = 0;
        int oldAlpha = alpha;
        int bestMove = BoardConstants.NO_MOVE;
        score = -INF;
        int pvMove = PVTable.probePVTable(boardStructure);

        for (moveNum = 0; moveNum < moveList.count; moveNum++) {

            pickNextMove(moveList, moveNum);

            if (!MakeMove.makeMove(boardStructure, moveList.moves[moveNum].move))
                continue;

            legal++;
            score = -quiescenceSearch(boardStructure, searchEntry, -beta, -alpha);
            MakeMove.takeMove(boardStructure);

            if (searchEntry.isStopped) {
                return 0;
            }

            if (score > alpha) {
                if (score >= beta) {
                    if (legal == 1) {
                        searchEntry.failHighFirst++;
                    }
                    searchEntry.failHigh++;

                    return beta;
                }
                alpha = score;
                bestMove = moveList.moves[moveNum].move;
            }
        }

        if (alpha != oldAlpha) {
            PVTable.storePVMove(boardStructure, bestMove);
        }

        return alpha;
    }

    static int alphaBeta(BoardStructure boardStructure, SearchEntry searchEntry,
                         int alpha, int beta, int depth, boolean isNull) {
        if (depth == 0) {
            return quiescenceSearch(boardStructure, searchEntry, alpha, beta);
        }

        if ((searchEntry.nodes & 2047) == 0) {
            checkUp(searchEntry);
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

            if (searchEntry.isStopped) {
                return 0;
            }

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
