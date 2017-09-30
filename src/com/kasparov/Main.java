package com.kasparov;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        BoardStructure boardStructure = new BoardStructure();
        boardStructure.initSqr120AndSqr64();
        boardStructure.initBitMasks();
        boardStructure.initHashKeys();

        long bitboard = 0L;

//        System.out.println("Start:");
//        bitboard |= (1L << boardStructure.sqr64(BoardSquare.D2.value));
//        bitboard |= (1L << boardStructure.sqr64(BoardSquare.D3.value));
//        bitboard |= (1L << boardStructure.sqr64(BoardSquare.D4.value));
//        bitboard |= (1L << boardStructure.sqr64(BoardSquare.D5.value));

//        for (int i = 0; i < 64; i++) {
//            boardStructure.printBitBoard(boardStructure.clearMask[i]);
//            System.out.println();
//        }

//        bitboard = boardStructure.setBit(bitboard, 61);
//        boardStructure.printBitBoard(bitboard);

//        Random r = new Random();
//        int p1 = r.nextInt();
//        int p2 = r.nextInt();
//        int p3 = r.nextInt();
//        int p4 = r.nextInt();
//
//        System.out.println(p1);
//        System.out.println(p2);
//        System.out.println(p3);
//        System.out.println(p4);
//
//        int key = p1 ^ p2 ^ p3 ^ p4;
//        int temp = p1;
//        temp ^= p3;
//        temp ^= p4;
//        temp ^= p2;
//
//        System.out.println("key: " + key);
//        System.out.println("tempkey: " + temp);
    }
}
