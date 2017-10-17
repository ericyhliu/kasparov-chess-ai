package kasparov;

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
            if (boardStructure.positionKey == boardStructure.history[i].getPosKey())
                return true;
        }
        return false;
    }

    static void searchPosition(BoardStructure boardStructure, SearchEntry searchEntry) {
        int bestMove = BoardConstants.NO_MOVE;
        int bestScore = -INF;
        int pvMoves;
        clearForSearch(boardStructure, searchEntry);

        for (int currentDepth = 1; currentDepth <= searchEntry.getDepth(); currentDepth++) {
            bestScore = alphaBeta(boardStructure, searchEntry, -INF, INF, currentDepth, true);

            if (searchEntry.isStopped()) {
                break;
            }

            pvMoves = PVTable.getPVLine(boardStructure, currentDepth);
            bestMove = boardStructure.pvArray[0];

            System.out.printf("info score cp %d depth %d nodes %d time %d ",
                    bestScore, currentDepth, searchEntry.getNodes(),
                    Time.getTimeInMilliseconds() - searchEntry.getStartTime());

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

        searchEntry.setStopped(false);
        searchEntry.setNodes(0);
        searchEntry.setFailHigh(0);
        searchEntry.setFailHighFirst(0);
    }

    static void checkUp(SearchEntry searchEntry) {
        if (searchEntry.isTimeSet() && Time.getTimeInMilliseconds() > searchEntry.getStopTime()) {
            searchEntry.setStopped(true);
        }
    }

    static int quiescenceSearch(BoardStructure boardStructure, SearchEntry searchEntry,
                                int alpha, int beta) {
        if ((searchEntry.getNodes() & 2047) == 0) {
            checkUp(searchEntry);
        }

        searchEntry.setNodes(searchEntry.getNodes()+1);

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

            if (!MakeMove.makeMove(boardStructure, moveList.moves[moveNum].getMove()))
                continue;

            legal++;
            score = -quiescenceSearch(boardStructure, searchEntry, -beta, -alpha);
            MakeMove.takeMove(boardStructure);

            if (searchEntry.isStopped()) {
                return 0;
            }

            if (score > alpha) {
                if (score >= beta) {
                    if (legal == 1) {
                        searchEntry.setFailHighFirst(searchEntry.getFailHighFirst()+1);
                    }
                    searchEntry.setFailHigh(searchEntry.getFailHigh()+1);

                    return beta;
                }
                alpha = score;
                bestMove = moveList.moves[moveNum].getMove();
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

        if ((searchEntry.getNodes() & 2047) == 0) {
            checkUp(searchEntry);
        }

        searchEntry.setNodes(searchEntry.getNodes()+1);

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
                if (moveList.moves[moveNum].getMove() == pvMove) {
                    moveList.moves[moveNum].setScore(2000000);
                    break;
                }
            }
        }

        for (int moveNum = 0; moveNum < moveList.count; moveNum++) {

            pickNextMove(moveList, moveNum);

            if (!MakeMove.makeMove(boardStructure, moveList.moves[moveNum].getMove()))
                continue;

            legal++;
            score = -alphaBeta(boardStructure, searchEntry, -beta, -alpha, depth-1, true);
            MakeMove.takeMove(boardStructure);

            if (searchEntry.isStopped()) {
                return 0;
            }

            if (score > alpha) {
                if (score >= beta) {
                    if (legal == 1) {
                        searchEntry.setFailHighFirst(searchEntry.getFailHighFirst()+1);
                    }
                    searchEntry.setFailHigh(searchEntry.getFailHigh()+1);


                    if ((moveList.moves[moveNum].getMove() & MoveUtils.MOVE_FLAG_CAPTURE) == 0) {
                        boardStructure.searchKillers[1][boardStructure.ply] =
                                boardStructure.searchKillers[0][boardStructure.ply];
                        boardStructure.searchKillers[0][boardStructure.ply] =
                                moveList.moves[moveNum].getMove();
                    }

                    return beta;
                }
                alpha = score;
                bestMove = moveList.moves[moveNum].getMove();

                if ((moveList.moves[moveNum].getMove() & MoveUtils.MOVE_FLAG_CAPTURE) == 0) {
                    boardStructure.searchHistory[boardStructure.pieces[MoveUtils.from(bestMove)]][MoveUtils.to(bestMove)]
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
            if (moveList.moves[i].getScore() > bestScore) {
                bestScore = moveList.moves[i].getScore();
                bestNum = i;
            }
        }

        temp = moveList.moves[moveNum];
        moveList.moves[moveNum] = moveList.moves[bestNum];
        moveList.moves[bestNum] = temp;
    }

}
