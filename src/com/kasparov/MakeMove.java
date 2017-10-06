package com.kasparov;

public class MakeMove {

    static long hashPiece(BoardStructure boardStructure, int piece, int sqr) {
        return boardStructure.positionKey ^ boardStructure.pieceKeys[piece][sqr];
    }

    static long hashLong(BoardStructure boardStructure) {
        return boardStructure.positionKey ^ boardStructure.castleKeys[boardStructure.castlePerm];
    }

    static long hashSide(BoardStructure boardStructure) {
        return boardStructure.positionKey ^ boardStructure.sideKey;
    }

    static long hashEnPassant(BoardStructure boardStructure) {
        return boardStructure.positionKey ^ boardStructure.pieceKeys[BoardPiece.EMPTY.value][boardStructure.enPassant];
    }

    static int[] castlePerm = {
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 13, 15, 15, 15, 12, 15, 15, 14, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15,  7, 15, 15, 15,  3, 15, 15, 11, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15
    };


}
