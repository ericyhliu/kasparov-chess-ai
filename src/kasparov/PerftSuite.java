package kasparov;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Runs the perft suite tests.
 *
 * @author Eric Liu
 */
public class PerftSuite {

    /**
     * Perft suite size.
     */
    protected static final int PERFT_SUITE_SIZE = 126;

    /**
     * Perft suite depth.
     */
    protected static final int PERFT_SUITE_DEPTH = 6;

    /**
     * Perft suite file location.
     */
    protected static final String PERFT_SUITE_FILE = "src/perft/perftsuite.txt";

    /**
     * Parses the perft suite tests from file and returns an array of
     * PerftSuiteTests.
     *
     * @return array of PerftSuiteTests
     */
    protected static PerftSuiteTest[] generatePerftSuiteTests() {
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
            System.out.println("File Not Found: '" +
                    PERFT_SUITE_FILE + "'");
            return null;
        } catch (IOException e) {
            System.out.println("Error reading file '" +
                    PERFT_SUITE_FILE + "'");
            return null;
        }
    }

    /**
     * Runs all PerftSuiteTests.
     */
    public static void main(String[] args) {
        PerftSuiteTest[] tests = generatePerftSuiteTests();
        PerftTest pf;
        BoardStructure boardStructure;
        long result;
        for (int i = 0; i < PERFT_SUITE_SIZE; i++) {
            for (int j = 1; j <= PERFT_SUITE_DEPTH; j++) {
                boardStructure = new BoardStructure();
                boardStructure.parseFEN(tests[i].getFen());
                boardStructure.updateListMaterials();

                pf = new PerftTest();
                result = pf.perftTest(boardStructure, j);
                System.out.print("Test " + (i+1) + ", " + j + ": ");

                if (tests[i].getDepthCount(j-1) == result)
                    System.out.println("Passed " +
                            (tests[i].getDepthCount(j-1) + " " + result));
                else
                    System.out.println("Failed " +
                            (tests[i].getDepthCount(j-1) + " " + result));
            }
        }
    }
}
