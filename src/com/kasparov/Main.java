package com.kasparov;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BoardStructure boardStructure = new BoardStructure();
        boardStructure.initSqr120AndSqr64();
        boardStructure.initBitMasks();
        boardStructure.initHashKeys();
        boardStructure.initFileAndRankBoard();
        boardStructure.initHistory();

        boardStructure.parseFEN(BoardConstants.STARTING_FEN);
        boardStructure.updateListMaterials();

        boardStructure.printBoard();
        PerftTest pf = new PerftTest();
        pf.perftTest(boardStructure, 3);

    }
}
