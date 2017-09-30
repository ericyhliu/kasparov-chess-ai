package com.kasparov;

public class Main {

    public static void main(String[] args) {
        BoardStructure boardStructure = new BoardStructure();
        boardStructure.initSqr120AndSqr64();
        boardStructure.initBitMasks();

        long bitboard = 0L;

//        System.out.println("Start:");
//        bitboard |= (1L << boardStructure.sqr64(BoardSquare.D2.value));
//        bitboard |= (1L << boardStructure.sqr64(BoardSquare.D3.value));
//        bitboard |= (1L << boardStructure.sqr64(BoardSquare.D4.value));
//        bitboard |= (1L << boardStructure.sqr64(BoardSquare.D5.value));

        for (int i = 0; i < 64; i++) {
            boardStructure.printBitBoard(boardStructure.clearMask[i]);
            System.out.println();
        }

//        bitboard = boardStructure.setBit(bitboard, 61);
//        boardStructure.printBitBoard(bitboard);


    }
}
