package kasparov;

/**
 * A test for the PerftSuite.
 *
 * @author Eric Liu
 */
public class PerftSuiteTest {

    private String fen;
    private long[] depthCount;

    /**
     * Initializes an empty PerftSuiteTest.
     */
    protected PerftSuiteTest() {}

    /**
     * Initializes a PerftSuiteTest with fen and depth count.
     *
     * @param fen
     * @param depthCount
     */
    protected PerftSuiteTest(String fen, long[] depthCount) {
        this.fen = fen;
        this.depthCount = depthCount;
    }

    /**
     * Getter for fen.
     *
     * @return fen
     */
    protected String getFen() {
        return fen;
    }

    /**
     * Getter for depth count.
     *
     * @return depthCount
     */
    protected long[] getDepthCount() {
        return depthCount;
    }

    /**
     * Setter for fen.
     *
     * @param fen
     */
    protected void setFen(String fen) {
        this.fen = fen;
    }

    /**
     * Setter for depth count.
     *
     * @param depthCount
     */
    protected void setDepthCount(long[] depthCount) {
        this.depthCount = depthCount;
    }

    /**
     * String representation of this instance of PerftSuiteTest.
     *
     * @return a String representation of this instance of PerftSuiteTest
     */
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
