package com.kasparov;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        BoardStructure boardStructure = new BoardStructure();
        boardStructure.initSqr120AndSqr64();
        boardStructure.initBitMasks();
        boardStructure.initHashKeys();
        boardStructure.initFileAndRankBoard();
        boardStructure.initHistory();
        MoveGenerator.initMvvLva();

        boardStructure.parseFEN(BoardConstants.FEN22);
        boardStructure.updateListMaterials();

        String input;
        SearchEntry searchEntry = new SearchEntry();
        Scanner in = new Scanner(System.in);

        int move;
        while (true) {
            boardStructure.printBoard();
            System.out.print("Please enter a move: ");
            input = in.next();

            if (input.charAt(0) == 'q') {
                break;
            } else if (input.charAt(0) == 't') {
                MakeMove.takeMove(boardStructure);
                continue;
            } else if (input.charAt(0) == 's') {
                searchEntry.depth = 6;
                searchEntry.startTime = Time.getTimeInMilleseconds();
                searchEntry.stopTime = Time.getTimeInMilleseconds() + 2000000;
                Search.searchPosition(boardStructure, searchEntry);
            } else {
                move = Move.parseMove(boardStructure, input);
                if (move != BoardConstants.NO_MOVE) {
                    PVTable.storePVMove(boardStructure, move);
                    MakeMove.makeMove(boardStructure, move);
//                    if (Search.isRepetition(boardStructure))
//                        System.out.println("Repetition Seen");
                } else {
                    System.out.println("Move not parsed: " + move);
                }
            }
        }

    }
}
