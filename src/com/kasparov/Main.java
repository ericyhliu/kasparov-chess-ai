package com.kasparov;

public class Main {

    public static void main(String[] args) {
        BoardStructure boardStructure = new BoardStructure();
        boardStructure.initSqr120AndSqr64();

        long bitboard = 0L;

        System.out.println("Start:");
        boardStructure.printBitBoard(bitboard);

        bitboard |= (1L << boardStructure.sqr64(BoardSquare.D2.value));
        System.out.println("D2 added:");
        boardStructure.printBitBoard(bitboard);

        bitboard |= (1L << boardStructure.sqr64(BoardSquare.D3.value));
        System.out.println("D3 added:");
        boardStructure.printBitBoard(bitboard);

        bitboard |= (1L << boardStructure.sqr64(BoardSquare.D4.value));
        System.out.println("D4 added:");
        boardStructure.printBitBoard(bitboard);

        bitboard |= (1L << boardStructure.sqr64(BoardSquare.D5.value));
        System.out.println("D5 added:");
        boardStructure.printBitBoard(bitboard);


        int sqr64 = 0;
        while (bitboard != 0) {
            long[] popResult = boardStructure.popBit(bitboard);
            System.out.println("Index: " + popResult[1]);
            bitboard = popResult[0];
            boardStructure.printBitBoard(bitboard);
        }
    }
}
