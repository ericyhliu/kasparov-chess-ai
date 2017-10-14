package com.kasparov;

/**
 * Search entry.
 *
 * @author Eric Liu
 */
public class SearchEntry {

    long startTime;
    long stopTime;
    int depth;
    int depthSet;
    boolean timeSet;
    long nodes;
    boolean quit;
    boolean isStopped;
    int movesToGo;
    int infinite;
    double failHigh;
    double failHighFirst;

}
