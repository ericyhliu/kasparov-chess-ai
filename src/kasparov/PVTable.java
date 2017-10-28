package kasparov;

/**
 * Principal variation table.
 *
 * @author Eric Liu
 */
public class PVTable {

    /**
     * Stores the principal variations.
     */
    private PVEntry[] pvEntryTable;
    private int numEntries;


    /**
     * Initializes an empty PVTable.
     */
    protected PVTable() {}


    /**
     * Initializes entries in PVTable.
     */
    protected void initPVTable() {
        numEntries = 150000;
        pvEntryTable = new PVEntry[numEntries];
        for (int i = 0; i < numEntries; i++)
            pvEntryTable[i] = new PVEntry();
        clearPVTable();
    }

    /**
     * Clears the PVTable.
     */
    protected void clearPVTable() {
        for (int i = 0; i < this.numEntries; i++) {
            pvEntryTable[i].setPosKey(0);
            pvEntryTable[i].setMove(BoardUtils.NO_MOVE);
        }
    }

    /**
     * Stores move as a PVEntry into the PVTable.
     *
     * @param boardStructure
     * @param move
     */
    protected static void storePVMove(BoardStructure boardStructure, int move) {
        int index = (int) boardStructure.getPositionKey() % boardStructure.getPVTable().numEntries;
        if (index < 0)
            index *= -1;
        boardStructure.getPVTable().pvEntryTable[index].setMove(move);
        boardStructure.getPVTable().pvEntryTable[index].setPosKey(boardStructure.getPositionKey());
    }

    /**
     * Probes PVTable.
     *
     * @param boardStructure
     * @return PVEntry into the PVTable
     */
    protected static int probePVTable(BoardStructure boardStructure) {
        int index = (int) boardStructure.getPositionKey() % boardStructure.getPVTable().numEntries;
        if (index < 0)
            index *= -1;
        if (boardStructure.getPVTable().pvEntryTable[index].getPosKey() ==
                boardStructure.getPositionKey())
            return boardStructure.getPVTable().pvEntryTable[index].getMove();

        return BoardUtils.NO_MOVE;
    }

    /**
     * Get PVTable line.
     *
     * @param boardStructure
     * @param depth
     * @return PVTable line
     */
    protected static int getPVLine(BoardStructure boardStructure, int depth) {
        int move = probePVTable(boardStructure);
        int count = 0;
        while (move != BoardUtils.NO_MOVE && count < depth) {
            if (MoveGenerator.moveExists(boardStructure, move)) {
                MakeMove.makeMove(boardStructure, move);
                boardStructure.setPVArrayEntry(move, count);
                count++;
            } else {
                break;
            }
            move = probePVTable(boardStructure);
        }

        while (boardStructure.getPly() > 0)
            MakeMove.takeMove(boardStructure);
        return count;
    }

}
