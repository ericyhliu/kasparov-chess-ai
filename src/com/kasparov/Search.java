package com.kasparov;

/**
 * For searching for the position.
 *
 * @author Eric Liu
 */
public class Search {

    static boolean isRepetition(BoardStructure boardStructure) {
        for (int i = boardStructure.historyPly - boardStructure.fiftyMove;
             i < boardStructure.historyPly - 1; i++) {
            if (boardStructure.positionKey == boardStructure.history[i].posKey)
                return true;
        }
        return false;
    }

    static void searchPosition(BoardStructure boardStructure, SearchEntry searchEntry) {
        // handle iterative deepening
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
                         int alpha, int beta, int depth, int doNull) {
        return 0;
    }
}
