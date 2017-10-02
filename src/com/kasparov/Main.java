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

        System.out.println();

        int move = 0;
        int from = 6;
        int to = 12;
        int cap = BoardPiece.WHITE_ROOK.value;
        int prom = BoardPiece.BLACK_ROOK.value;

        move = (from | (to << 7) | (cap << 14) | (prom << 20));

        System.out.println("Decimal: " + move);
        System.out.println("    Hex: " + Integer.toHexString(move));
        System.out.println(" Binary: " + Integer.toBinaryString(move));
        System.out.println("   From: " + Move.from(move));
        System.out.println("     To: " + Move.to(move));
        System.out.println("Capture: " + Move.captured(move));
        System.out.println("  Promo: " + Move.promoted(move));

        //move |= Move.moveFlagPawnStart;

        System.out.println("Is Pawn Start: " + ((move & Move.moveFlagPawnStart) != 0 ? "Yes" : "No"));


    }
}
