package com.kasparov;

/**
 * Move structure.
 *
 * @author Eric Liu
 */
public class Move {
    int move;
    int score;

    static int from(int move) {
        return move & 0x7F;
    }

    static int to(int move) {
        return (move >> 7) & 0x7F;
    }

    static int captured(int move) {
        return (move >> 14) & 0xF;
    }

    static int promoted(int move) {
        return (move >> 20) & 0xF;
    }

    static int moveFlagEnPassant = 0x40000;

    static int moveFlagPawnStart = 0x80000;

    static int moveFlagCastle = 0x1000000;

    static int moveFlagCapture = 0x7C000;

    static int moveFlagPromoted = 0xF00000;

    public Move() {}

    public Move(int move, int score) {
        this.move = move;
        this.score = score;
    }

}
