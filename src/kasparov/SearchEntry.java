package kasparov;

/**
 * Search entry.
 *
 * @author Eric Liu
 */
public class SearchEntry {

    private long startTime;
    private long stopTime;
    private long nodes;
    private int depth;
    private int depthSet;
    private int movesToGo;
    private int infinite;
    private boolean isTimeSet;
    private boolean isQuit;
    private boolean isStopped;
    private double failHigh;
    private double failHighFirst;

    /**
     * Initializes an empty SearchEntry.
     */
    protected SearchEntry() {
        startTime = -1;
        stopTime = -1;
        nodes = -1;
        depth = -1;
        depthSet = - 1;
        movesToGo = -1;
        infinite = -1;
        isTimeSet = false;
        isQuit = false;
        isStopped = false;
        failHigh = -1;
        failHighFirst = -1;
    }

    /**
     * Getter for start time.
     *
     * @return startTime
     */
    protected long getStartTime() {
        return startTime;
    }

    /**
     * Getter for stop time.
     *
     * @return stopTime
     */
    protected long getStopTime() {
        return stopTime;
    }

    /**
     * Getter for nodes.
     *
     * @return nodes
     */
    protected long getNodes() {
        return nodes;
    }

    /**
     * Getter for depth.
     *
     * @return depth
     */
    protected int getDepth() {
        return depth;
    }

    /**
     * Getter for depth set.
     *
     * @return depthSet
     */
    protected int getDepthSet() {
        return depthSet;
    }

    /**
     * Getter for moves to go.
     *
     * @return movesToGo
     */
    protected int getMovesToGo() {
        return movesToGo;
    }

    /**
     * Getter for infinite.
     *
     * @return infinite
     */
    protected int getInfinite() {
        return infinite;
    }

    /**
     * Checks if time is set.
     *
     * @return isTimeSet
     */
    protected boolean isTimeSet() {
        return isTimeSet;
    }

    /**
     * Checks if program has quit.
     *
     * @return isQuit
     */
    protected boolean isQuit() {
        return isQuit;
    }

    /**
     * Checks if search has stopped.
     *
     * @return isStopped
     */
    protected boolean isStopped() {
        return isStopped;
    }

    /**
     * Getter for fail high.
     *
     * @return failHigh
     */
    protected double getFailHigh() {
        return failHigh;
    }

    /**
     * Getter for fail high first.
     *
     * @return failHighFirst
     */
    protected double getFailHighFirst() {
        return failHighFirst;
    }

    /**
     * Setter for start time.
     *
     * @param startTime
     */
    protected void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * Setter for stop time.
     *
     * @param stopTime
     */
    protected void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    /**
     * Setter for nodes.
     *
     * @param nodes
     */
    protected void setNodes(long nodes) {
        this.nodes = nodes;
    }

    /**
     * Setter for depth.
     *
     * @param depth
     */
    protected void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Setter for depth set.
     *
     * @param depthSet
     */
    protected void setDepthSet(int depthSet) {
        this.depthSet = depthSet;
    }

    /**
     * Setter for moves to go.
     *
     * @param movesToGo
     */
    protected void setMovesToGo(int movesToGo) {
        this.movesToGo = movesToGo;
    }

    /**
     * Setter for infinite.
     *
     * @param infinite
     */
    protected void setInfinite(int infinite) {
        this.infinite = infinite;
    }

    /**
     * Setter for time set.
     *
     * @param isTimeSet
     */
    protected void setTimeSet(boolean isTimeSet) {
        this.isTimeSet = isTimeSet;
    }

    /**
     * Setter for quit.
     *
     * @param isQuit
     */
    protected void setQuit(boolean isQuit) {
        this.isQuit = isQuit;
    }

    /**
     * Setter for stopped.
     *
     * @param isStopped
     */
    protected void setStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

    /**
     * Setter for fail high.
     *
     * @param failHigh
     */
    protected void setFailHigh(double failHigh) {
        this.failHigh = failHigh;
    }

    /**
     * Setter for fail high first.
     *
     * @param failHighFirst
     */
    protected void setFailHighFirst(double failHighFirst) {
        this.failHighFirst = failHighFirst;
    }
}
