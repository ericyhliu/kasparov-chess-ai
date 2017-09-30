package com.kasparov;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        BoardStructure boardStructure = new BoardStructure();
        boardStructure.initSqr120AndSqr64();
        boardStructure.initBitMasks();
        boardStructure.initHashKeys();

        boardStructure.parseFEN(BoardConstants.startingFEN);

        long bitboard = 0L;
    }
}
