package com.kasparov;

public class Main {

    public static void main(String[] args) {
        BoardStructure boardStructure = new BoardStructure();
        boardStructure.initSqr120AndSqr64();
        boardStructure.initBitMasks();
        boardStructure.initHashKeys();
        boardStructure.initFileAndRankBoard();

        boardStructure.parseFEN(BoardConstants.FEN5);
        boardStructure.printBoard();

        boardStructure.updateListMaterials();

        System.out.println("White attacking:");
        SquareAttacked.showSqrAttackedBySide(BoardColor.WHITE.value, boardStructure);

        System.out.println("Black attacking:");
        SquareAttacked.showSqrAttackedBySide(BoardColor.BLACK.value, boardStructure);


    }
}
