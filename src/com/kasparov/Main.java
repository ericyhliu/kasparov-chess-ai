package com.kasparov;

public class Main {

    public static void main(String[] args) {
        BoardStructure boardStructure = new BoardStructure();
        boardStructure.initSqr120AndSqr64();
        boardStructure.initBitMasks();
        boardStructure.initHashKeys();
        boardStructure.initFileAndRankBoard();

        boardStructure.parseFEN(BoardConstants.FEN4);
        boardStructure.printBoard();

        boardStructure.updateListMaterials();


        System.out.println("\nWhite Pawns:");
        boardStructure.printBitBoard(boardStructure.pawns[BoardColor.WHITE.value]);

        System.out.println("\nBlack Pawns:");
        boardStructure.printBitBoard(boardStructure.pawns[BoardColor.BLACK.value]);

        System.out.println("\nBoth:");
        boardStructure.printBitBoard(boardStructure.pawns[BoardColor.BOTH.value]);

        System.out.println(boardStructure.checkBoard());



    }
}
