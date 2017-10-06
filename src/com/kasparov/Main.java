package com.kasparov;

public class Main {

    public static void main(String[] args) {
        BoardStructure boardStructure = new BoardStructure();
        boardStructure.initSqr120AndSqr64();
        boardStructure.initBitMasks();
        boardStructure.initHashKeys();
        boardStructure.initFileAndRankBoard();

        boardStructure.parseFEN(BoardConstants.FEN19);
        boardStructure.printBoard();

        boardStructure.updateListMaterials();

        System.out.println();

        MoveList moveList = new MoveList();

        MoveGenerator.generateAddMoves(boardStructure, moveList);

        MoveList.printMoveList(moveList, boardStructure);
    }
}
