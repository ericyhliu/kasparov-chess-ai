package com.kasparov;

import java.util.Scanner;

/**
 * UCI protocol.
 *
 * @author Eric Liu
 */
public class UCI {

    static final int INPUT_BUFFER = 400 * 6;

    static void parseGo(BoardStructure boardStructure, SearchEntry searchEntry, String line) {
        System.out.println("GO");
    }

    static void parsePosition(BoardStructure boardStructure, String line) {
        System.out.println("POS");
    }

    static void start() {
        Scanner in = new Scanner(System.in);
        String line;
        System.out.printf("id name %s\n", BoardConstants.PROGRAM_NAME);
        System.out.printf("id author %s\n", BoardConstants.PROGRAM_AUTHOR);
        System.out.printf("uciok\n");

        BoardStructure boardStructure = new BoardStructure();
        SearchEntry searchEntry = new SearchEntry();

        boardStructure.initSqr120AndSqr64();
        boardStructure.initBitMasks();
        boardStructure.initHashKeys();
        boardStructure.initFileAndRankBoard();
        boardStructure.initHistory();
        boardStructure.updateListMaterials();
        MoveGenerator.initMvvLva();

        while (true) {
            line = in.nextLine();

            if (line.equals("isready")) {
                System.out.printf("readyok\n");
                continue;
            } else if (line.equals("position")) {
                parsePosition(boardStructure, line);
            } else if (line.equals("ucinewgame")) {
                parsePosition(boardStructure, "position startpos\n");
            } else if (line.equals("go")) {
                parseGo(boardStructure, searchEntry, line);
            } else if (line.equals("quit")) {
                searchEntry.quit = true;
                break;
            } else if (line.equals("uci")) {
                System.out.printf("id name %s\n", BoardConstants.PROGRAM_NAME);
                System.out.printf("id author %s\n", BoardConstants.PROGRAM_AUTHOR);
                System.out.printf("uciok\n");
            }

            if (searchEntry.quit) {
                break;
            }
        }
    }

}
