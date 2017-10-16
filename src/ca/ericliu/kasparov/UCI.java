package ca.ericliu.kasparov;

import java.util.Scanner;

/**
 * UCI protocol.
 *
 * @author Eric Liu
 */
public class UCI {

    static void parseGo(BoardStructure boardStructure, SearchEntry searchEntry, String line) {
        int ptr = 3;
        if (ptr >= line.length())
            return;
        String s = line.substring(ptr, line.length()).trim();

        Scanner in = new Scanner(s);
        int depth = -1;
        int movesToGo = 30;
        int moveTime = -1;
        int time = -1;
        int inc = 0;
        searchEntry.timeSet = false;

        String arg;
        while (in.hasNext()) {
            arg = in.next();
            if (arg.equals("infinite"))
                return;

            if (arg.equals("winc") && boardStructure.side == BoardColor.WHITE.value)
                inc = Integer.parseInt(in.next());

            if (arg.equals("binc") && boardStructure.side == BoardColor.BLACK.value)
                inc = Integer.parseInt(in.next());

            if (arg.equals("wtime") && boardStructure.side == BoardColor.WHITE.value)
                time = Integer.parseInt(in.next());

            if (arg.equals("btime") && boardStructure.side == BoardColor.BLACK.value)
                time = Integer.parseInt(in.next());

            if (arg.equals("movestogo"))
                movesToGo = Integer.parseInt(in.next());

            if (arg.equals("movetime"))
                moveTime = Integer.parseInt(in.next());

            if (arg.equals("depth"))
                depth = Integer.parseInt(in.next());
        }

        if (moveTime != -1) {
            time = moveTime;
            movesToGo = 1;
        }

        searchEntry.startTime = Time.getTimeInMilleseconds();
        searchEntry.depth = depth;

        if (time != -1) {
            searchEntry.timeSet = true;
            time /= movesToGo;
            time -= 50;
            searchEntry.stopTime = searchEntry.startTime + time + inc;
        }

        if (depth == -1) {
            searchEntry.depth = BoardConstants.MAX_DEPTH;
        }

        System.out.printf("Time: %d  Start: %d  Stop: %d  Depth: %d  Timeset: %b\n",
                time, searchEntry.startTime, searchEntry.stopTime, searchEntry.depth, searchEntry.timeSet);
        Search.searchPosition(boardStructure, searchEntry);
    }

    static void parsePosition(BoardStructure boardStructure, String line) {
        int ptr = 9;
        if (ptr >= line.length())
            return;
        String s = line.substring(ptr, line.length()).trim();

        // position startpos
        if (s.equals("startpos")) {
            boardStructure.parseFEN(BoardConstants.STARTING_FEN);
            boardStructure.updateListMaterials();
            boardStructure.printBoard();
            return;
        }

        // position startpos moves <m1 m2 ... mn>
        if (s.startsWith("startpos")) {
            ptr += 9;
            if (ptr >= line.length())
                return;
            s = line.substring(ptr, line.length());
            if (!s.startsWith("moves"))
                return;
            ptr += 6;
            if (ptr >= line.length())
                return;
            s = line.substring(ptr, line.length());

            boardStructure.parseFEN(BoardConstants.STARTING_FEN);
            boardStructure.updateListMaterials();

            String[] moves = s.split(" ");
            for (int i = 0; i < moves.length; i++) {
                int move = Move.parseMove(boardStructure, moves[i]);
                if (move == BoardConstants.NO_MOVE)
                    break;
                MakeMove.makeMove(boardStructure, move);
                boardStructure.ply = 0;
            }
            boardStructure.printBoard();
            return;
        }

        // position fen <fenStr> [moves <m1 m2 ... mn>]
        if (s.startsWith("fen")) {
            ptr += 4;
            if (ptr >= line.length())
                return;
            s = line.substring(ptr, line.length());

            int movesIndex = line.indexOf("moves");

            // position fen <fenStr>
            if (movesIndex == -1) {
                boardStructure.parseFEN(s);
                boardStructure.updateListMaterials();
                boardStructure.printBoard();
                return;
            }

            // position fen <fenStr> moves <m1 m2 ... mn>
            String fen = line.substring(ptr, movesIndex-1);
            ptr = movesIndex + 6;
            if (ptr >= line.length())
                return;

            s = line.substring(ptr, line.length());

            boardStructure.parseFEN(fen);
            boardStructure.updateListMaterials();

            String[] moves = s.split(" ");
            for (int i = 0; i < moves.length; i++) {
                int move = Move.parseMove(boardStructure, moves[i]);
                if (move == BoardConstants.NO_MOVE)
                    break;
                MakeMove.makeMove(boardStructure, move);
                boardStructure.ply = 0;
            }
            boardStructure.printBoard();
            return;
        }
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
        MoveGenerator.initMvvLva();

        while (true) {
            line = in.nextLine();

            if (line.startsWith("isready")) {
                System.out.printf("readyok\n");
                continue;
            } else if (line.startsWith("position")) {
                parsePosition(boardStructure, line);
            } else if (line.equals("ucinewgame")) {
                parsePosition(boardStructure, "position startpos\n");
            } else if (line.startsWith("go")) {
                parseGo(boardStructure, searchEntry, line);
            } else if (line.startsWith("quit")) {
                searchEntry.quit = true;
                break;
            } else if (line.startsWith("uci")) {
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
