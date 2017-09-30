package com.kasparov;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        BoardStructure boardStructure = new BoardStructure();
        boardStructure.initSqr120AndSqr64();
        boardStructure.initBitMasks();
        boardStructure.initHashKeys();

        boardStructure.parseFEN(BoardConstants.startingFEN);
        boardStructure.printBoard();

        boardStructure.parseFEN(BoardConstants.FEN2);
        boardStructure.printBoard();

        boardStructure.parseFEN(BoardConstants.FEN3);
        boardStructure.printBoard();

        boardStructure.parseFEN(BoardConstants.FEN4);
        boardStructure.printBoard();


    }
}
