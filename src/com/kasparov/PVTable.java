package com.kasparov;

/**
 * Principle variation table.
 *
 * @author Eric Liu
 */
public class PVTable {

    static int pvSize = 0x100000 * 2;

    PVEntry[] pvEntryTable;

    int numEntries;

    public PVTable() {}

    public void initPVTable() {
        this.numEntries = 150000;
        this.pvEntryTable = new PVEntry[this.numEntries];
        for (int i = 0; i < this.numEntries; i++)
            this.pvEntryTable[i] = new PVEntry();
        System.out.println("PVTable initialization complete with " + this.numEntries + " entries");
        clearPVTable();
    }

    public void clearPVTable() {
        for (int i = 0; i < this.numEntries; i++) {
            this.pvEntryTable[i].posKey = 0;
            this.pvEntryTable[i].move = BoardConstants.NO_MOVE;
        }
    }

    static void storePVMove(BoardStructure boardStructure, int move) {
        int index = (int) boardStructure.positionKey % boardStructure.pvTable.numEntries;
        assert(index >= 0 && index <= boardStructure.pvTable.numEntries - 1);
        boardStructure.pvTable.pvEntryTable[index].move = move;
        boardStructure.pvTable.pvEntryTable[index].posKey = boardStructure.positionKey;
    }

    static long probePVTable(BoardStructure boardStructure) {
        int index = (int) boardStructure.positionKey % boardStructure.pvTable.numEntries;
        assert(index >= 0 && index <= boardStructure.pvTable.numEntries - 1);

        if (boardStructure.pvTable.pvEntryTable[index].posKey == boardStructure.positionKey)
            return boardStructure.pvTable.pvEntryTable[index].posKey;

        return BoardConstants.NO_MOVE;
    }

}
