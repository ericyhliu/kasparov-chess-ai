package com.kasparov;

/**
 * Generates moves.
 */
public class MoveGenerator {

    public void moveGen(BoardStructure boardStructure, MoveList moveList) {
        // Loop all pieces
            // If slider, loop each dir and add move
                // AddMove list.moves[list.count] = move
                // list.count++
    }

    public void addQuietMove(BoardStructure boardStructure, int move, MoveList moveList) {
        moveList.addMove(move);
    }

    public void addCaptureMove(BoardStructure boardStructure, int move, MoveList moveList) {

    }

    public void addEnPassantMove(BoardStructure boardStructure, int move, MoveList moveList) {

    }

    public void generateAddMoves(BoardStructure boardStructure, MoveList moveList) {
        moveList.setCount(0);
    }



}
