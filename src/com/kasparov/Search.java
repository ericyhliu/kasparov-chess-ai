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
