package com.kasparov;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        BoardStructure boardStructure = new BoardStructure();
        boardStructure.initSqr120AndSqr64();
        boardStructure.initBitMasks();
        boardStructure.initHashKeys();
        boardStructure.updateListMaterials();

        boardStructure.parseFEN(BoardConstants.startingFEN);
        boardStructure.printBoard();

    }
}
