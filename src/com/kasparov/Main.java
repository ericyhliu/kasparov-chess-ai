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

        boardStructure.parseFEN(BoardConstants.STARTING_FEN);
        boardStructure.updateListMaterials();

        String input;

        Scanner in = new Scanner(System.in);
        int move = BoardConstants.NO_MOVE;
        while (true) {
            boardStructure.printBoard();
            System.out.print("Please enter a move: ");
            input = in.next();

            if (input.charAt(0) == 'q') {
                break;
            } else if (input.charAt(0) == 't') {
                MakeMove.takeMove(boardStructure);
                continue;
            } else if (input.charAt(0) == 'p') {
                PerftTest pt = new PerftTest();
                pt.perftTest(boardStructure, 4);
            } else {
                move = Move.parseMove(boardStructure, input);
                if (move != BoardConstants.NO_MOVE) {
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
