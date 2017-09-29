package com.kasparov;

/**
 * Structure and properties of the chess board.
 *
 * @author Eric Liu
 */
public class BoardStructure {

    /**
     *
     */
    int[] pieces = new int[BoardConstants.BOARD_SQUARE_NUM];

    /**
     * 64 bits represents 8 x 8 board, where 1 denotes pawn at that
     * location, and 0 denotes no pawn at that location.
     */
    long[] pawns = new long[3];

    /**
     * Locations of the two kings.
     */
    int[] kingSquares = new int[2];

    /**
     * Current side to move.
     */
    int side;

    /**
     * Current active en passant square.
     */
    int enPassant;

    /**
     * Counter for the 50-move rule.
     */
    int fiftyMove;

    /**
     * Number of half moves in current search.
     */
    int ply;

    /**
     * Total number of half moves in entire game.
     */
    int historyPly;

    /**
     * Unique 64 bit key for the current position.
     */
    long positionKey;

    /**
     * Number of pieces, by piece type.
     */
    int[] pieceNum = new int[13];

    /**
     * Number of big pieces (any piece not a pawn), by color
     * (white, black, both).
     */
    int[] pieceBig = new int[3];

    /**
     * Number of major pieces (queens and rooks), by color
     * (white, black, both).
     */
    int[] pieceMajor = new int[3];

    /**
     * Number of minor pieces (bishops and knights), by color
     * (white, black, both).
     */
    int[] pieceMinor = new int[3];

    /**
     * Castle permissions.
     */
    int castlePerm;

    /**
     * Keeps track of the history of the game.
     */
    UndoStructure[] history = new UndoStructure[BoardConstants.MAX_GAME_MOVES];

    /**
     * Initializes an empty BoardStructure.
     */
    public BoardStructure() {}

    /**
     * Initializes a BoardStructure with given properties.
     *
     * @param pieces
     * @param pawns
     * @param kingSquares
     * @param side
     * @param enPassant
     * @param fiftyMove
     * @param ply
     * @param historyPly
     * @param positionKey
     * @param pieceNum
     * @param pieceBig
     * @param pieceMajor
     * @param pieceMinor
     * @param castlePerm
     * @param history
     */
    public BoardStructure(int[] pieces, 
                          long[] pawns, 
                          int[] kingSquares,
                          int side,
                          int enPassant,
                          int fiftyMove,
                          int ply,
                          int historyPly,
                          long positionKey,
                          int[] pieceNum,
                          int[] pieceBig,
                          int[] pieceMajor,
                          int[] pieceMinor,
                          int castlePerm,
                          UndoStructure[] history) {
        this.pieces = pieces;
        this.pawns = pawns;
        this.kingSquares = kingSquares;
        this.side = side;
        this.enPassant = enPassant;
        this.fiftyMove = fiftyMove;
        this.ply = ply;
        this.historyPly = historyPly;
        this.positionKey = positionKey;
        this.pieceNum = pieceNum;
        this.pieceBig = pieceBig;
        this.pieceMajor = pieceMajor;
        this.pieceMinor = pieceMinor;
        this.castlePerm = castlePerm;
        this.history = history;
    }
}
