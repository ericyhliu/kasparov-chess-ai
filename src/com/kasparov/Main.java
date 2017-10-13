package com.kasparov;

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

        String input;
        while (true) {
            boardStructure.printBoard();
            System.out.println("Please enter a move: ");


        }

    }
}
