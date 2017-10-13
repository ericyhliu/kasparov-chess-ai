package com.kasparov;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PerftSuite {

    static class PerftSuiteTest {
        String fen;
        long[] depthCount;

        public PerftSuiteTest(String fen, long[] depthCount) {
            this.fen = fen;
            this.depthCount = depthCount;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(fen);
            sb.append("[");
            for (long dc : depthCount) {
                sb.append(" ");
                sb.append(dc);
                sb.append(" ");
            }
            sb.append("]");
            return sb.toString();
        }
    }

    static final int PERFT_SUITE_SIZE = 126;
    static final String PERFT_SUITE_FILE = "src/perft/perftsuite.txt";

    static PerftSuiteTest[] generatePerftSuiteTests() {
        PerftSuiteTest[] tests = new PerftSuiteTest[PERFT_SUITE_SIZE];

        String line;
        String[] parts;
        try {
            FileReader fileReader = new FileReader(PERFT_SUITE_FILE);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                parts = line.split(";");
                long[] depthCount = new long[6];
                for (int j = 0; j < 6; j++)
                    depthCount[j] = Long.parseLong(parts[j+1].split(" ")[1]);
                tests[i] = new PerftSuiteTest(parts[0], depthCount);
                i++;
            }
            bufferedReader.close();

            return tests;
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found: '" + PERFT_SUITE_FILE + "'");
            return null;
        } catch (IOException e) {
            System.out.println("Error reading file '" + PERFT_SUITE_FILE + "'");
            return null;
        }
    }

    public static void main(String[] args) {
        PerftSuiteTest[] tests = generatePerftSuiteTests();
        PerftTest pf;
        BoardStructure boardStructure;
        long result;
        for (int i = 0; i < PERFT_SUITE_SIZE; i++) {
            for (int j = 1; j <= 6; j++) {
                boardStructure = new BoardStructure();
                boardStructure.initSqr120AndSqr64();
                boardStructure.initBitMasks();
                boardStructure.initHashKeys();
                boardStructure.initFileAndRankBoard();
                boardStructure.initHistory();
                boardStructure.parseFEN(tests[i].fen);
                boardStructure.updateListMaterials();

                pf = new PerftTest();
                result = pf.perftTest(boardStructure, j);
                System.out.print("Test " + (i+1) + ", " + j + ": ");

                if (tests[i].depthCount[j-1] == result)
                    System.out.println("Passed " + (tests[i].depthCount[j-1]) + " " + result);
                else
                    System.out.println("Failed " + (tests[i].depthCount[j-1]) + " " + result);
            }
        }
    }
}
