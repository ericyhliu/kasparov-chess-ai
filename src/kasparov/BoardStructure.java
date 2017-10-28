package kasparov;

import java.util.Random;

/**
 * Structure and properties of the chess board.
 *
 * @author Eric Liu
 */
public class BoardStructure {

    /**
     * Represents the board pieces on the entire size 120 board.
     */
    private int[] pieces = new int[BoardConstants.BOARD_SQR_NUM];

    /**
     * 64 bits represents 8 x 8 board, where 1 denotes pawn at that
     * location, and 0 denotes no pawn at that location.
     */
    private long[] pawns = new long[3];

    /**
     * Locations of the two kings.
     */
    private int[] kingSqr = new int[2];

    /**
     * Current side to move.
     */
    private int side;

    /**
     * Current active en passant square.
     */
    private int enPassant;

    /**
     * Counter for the 50-move rule.
     */
    private int fiftyMove;

    /**
     * Number of half moves in current search.
     */
    private int ply;

    /**
     * Total number of half moves in entire game.
     */
    private int historyPly;

    /**
     * Unique 64 bit key for the current position.
     */
    private long positionKey;

    /**
     * Number of pieces, by piece type.
     */
    private int[] pieceNum = new int[13];

    /**
     * Number of big pieces (any piece not a pawn), by color
     * (white, black, both).
     */
    private int[] pieceBig = new int[2];

    /**
     * Number of major pieces (queens and rooks), by color
     * (white, black, both).
     */
    private int[] pieceMajor = new int[2];

    /**
     * Number of minor pieces (bishops and knights), by color
     * (white, black, both).
     */
    private int[] pieceMinor = new int[2];

    /**
     * Material.
     */
    private int[] material = new int[2];

    /**
     * Castle permissions.
     */
    private int castlePerm;

    /**
     * Keeps track of the history of the game.
     */
    private UndoStructure[] history = new UndoStructure[BoardConstants.MAX_GAME_MOVES];

    /**
     * Maps square index from size 120 board to size 64 board.
     */
    private int[] sqr120ToSqr64 = new int[BoardConstants.BOARD_SQR_NUM];

    /**
     * Maps square index from size 64 board to size 120 board.
     */
    private int[] sqr64ToSqr120 = new int[64];

    /**
     * Piece list.
     */
    private int[][] pieceList = new int[13][10];

    /**
     * Set mask.
     */
    private long[] setMask = new long[64];

    /**
     * Clear mask.
     */
    private long[] clearMask = new long[64];

    /**
     * Piece keys.
     */
    private long[][] pieceKeys = new long[13][120];

    /**
     * Side key.
     */
    private long sideKey;

    /**
     * Castle keys.
     */
    private long[] castleKeys = new long[16];

    /**
     * Files board.
     */
    private int[] fileBoard = new int[BoardConstants.BOARD_SQR_NUM];

    /**
     * Ranks board.
     */
    private int[] rankBoard = new int[BoardConstants.BOARD_SQR_NUM];

    /**
     * Principal variation table.
     */
    private PVTable pvTable;

    /**
     * Principal variation array.
     */
    private int[] pvArray = new int[BoardConstants.MAX_DEPTH];

    /**
     * Search history.
     */
    private int[][] searchHistory = new int[13][BoardConstants.BOARD_SQR_NUM];
    private int[][] searchKillers = new int[2][BoardConstants.MAX_DEPTH];


    /**
     * Initializes an empty BoardStructure.
     */
    public BoardStructure() {
        initSqr120AndSqr64();
        initBitMasks();
        initHashKeys();
        initFileAndRankBoard();
        initHistory();
    }

    /**
     * Getter for pieces.
     *
     * @param square
     * @return piece at index square
     * @throws IndexOutOfBoundsException if square is invalid
     */
    protected int getPiece(int square) {
        if (square < 0 || square >= BoardConstants.BOARD_SQR_NUM)
            throw new IndexOutOfBoundsException("invalid index");
        return pieces[square];
    }

    /**
     * Setter for piece.
     *
     * @param piece
     * @param square
     * @throws IllegalArgumentException if piece is invalid
     * @throws IndexOutOfBoundsException if square is invalid
     */
    protected void setPiece(int piece, int square) {
        if (piece < BoardPiece.EMPTY.value || piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("invalid piece");
        if (square < 0 || square >= BoardConstants.BOARD_SQR_NUM)
            throw new IndexOutOfBoundsException("invalid square");
        pieces[square] = piece;
    }

    /**
     * Getter for pawns.
     *
     * @param color
     * @return pawn at index color
     * @throws IndexOutOfBoundsException if color is invalid
     */
    protected long getPawn(int color) {
        if (color < BoardColor.WHITE.value || color > BoardColor.BOTH.value)
            throw new IndexOutOfBoundsException("invalid color");
        return pawns[color];
    }

    /**
     * Setter for pawns.
     *
     * @param board
     * @param color
     * @throws IndexOutOfBoundsException if color is invalid
     */
    protected void setPawn(long board, int color) {
        if (color < BoardColor.WHITE.value || color > BoardColor.BOTH.value)
            throw new IndexOutOfBoundsException("invalid color");
        pawns[color] = board;
    }

    /**
     * Getter for king locations.
     *
     * @param color
     * @return king location
     * @throws IllegalArgumentException if color is invalid
     */
    protected int getKing(int color) {
        if (color != BoardColor.WHITE.value && color != BoardColor.BLACK.value)
            throw new IllegalArgumentException("invalid color");
        return kingSqr[color];
    }

    /**
     * Setter for king square.
     *
     * @param square
     * @param color
     * @throws IllegalArgumentException if color is invalid
     */
    protected void setKing(int square, int color) {
        if (color != BoardColor.WHITE.value && color != BoardColor.BLACK.value)
            throw new IllegalArgumentException("invalid color");
        kingSqr[color] = square;
    }

    /**
     * Getter for side.
     *
     * @return side
     */
    protected int getSide() {
        return side;
    }

    /**
     * Setter for side.
     *
     * @param side
     */
    protected void setSide(int side) {
        this.side = side;
    }

    /**
     * Getter for en passant.
     *
     * @return enPassant
     */
    protected int getEnPassant() {
        return enPassant;
    }

    /**
     * Setter for en passant.
     *
     * @param enPassant
     */
    protected void setEnPassant(int enPassant) {
        this.enPassant = enPassant;
    }

    /**
     * Getter for fifty move.
     *
     * @return fiftyMove
     */
    protected int getFiftyMove() {
        return fiftyMove;
    }

    /**
     * Setter for fifty move.
     *
     * @param fiftyMove
     */
    protected void setFiftyMove(int fiftyMove) {
        this.fiftyMove = fiftyMove;
    }

    /**
     * Getter for ply.
     *
     * @return ply
     */
    protected int getPly() {
        return ply;
    }

    /**
     * Setter for ply.
     *
     * @param ply
     */
    protected void setPly(int ply) {
        this.ply = ply;
    }

    /**
     * Getter for history ply.
     *
     * @return historyPly
     */
    protected int getHistoryPly() {
        return historyPly;
    }

    /**
     * Setter for history ply.
     *
     * @param historyPly
     */
    protected void setHistoryPly(int historyPly) {
        this.historyPly = historyPly;
    }

    /**
     * Getter for position key.
     *
     * @return positionKey
     */
    protected long getPositionKey() {
        return positionKey;
    }

    /**
     * Setter for position key.
     *
     * @param positionKey
     */
    protected void setPositionKey(long positionKey) {
        this.positionKey = positionKey;
    }

    /**
     * Getter for piece number.
     *
     * @param piece
     * @return pieceNum at index piece
     * @throws IllegalArgumentException if invalid piece
     */
    protected int getPieceNum(int piece) {
        if (piece < BoardPiece.EMPTY.value || piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("invalid piece");
        return pieceNum[piece];
    }

    /**
     * Setter for piece number.
     *
     * @param n
     * @param piece
     * @throws IllegalArgumentException if invalid piece
     */
    protected void setPieceNum(int n, int piece) {
        if (piece < BoardPiece.EMPTY.value || piece > BoardPiece.BLACK_KING.value)
            throw new IllegalArgumentException("invalid piece");
        pieceNum[piece] = n;
    }

    /**
     * Getter for big piece number.
     *
     * @param color
     * @return pieceBig at index color
     * @throws IllegalArgumentException if invalid color
     */
    protected int getPieceNumBig(int color) {
        if (color != BoardColor.WHITE.value && color != BoardColor.BLACK.value)
            throw new IllegalArgumentException("invalid color");
        return pieceBig[color];
    }

    /**
     * Setter for big piece number.
     *
     * @param n
     * @param color
     * @throws IllegalArgumentException if invalid color
     */
    protected void setPieceNumBig(int n, int color) {
        if (color != BoardColor.WHITE.value && color != BoardColor.BLACK.value)
            throw new IllegalArgumentException("invalid color");
        pieceBig[color] = n;
    }

    /**
     * Getter for major piece number.
     *
     * @param color
     * @return pieceMajor at index color
     * @throws IllegalArgumentException if invalid color
     */
    protected int getPieceNumMajor(int color) {
        if (color != BoardColor.WHITE.value && color != BoardColor.BLACK.value)
            throw new IllegalArgumentException("invalid color");
        return pieceMajor[color];
    }

    /**
     * Setter for major piece number.
     *
     * @param n
     * @param color
     * @throws IllegalArgumentException if invalid color
     */
    protected void setPieceNumMajor(int n, int color) {
        if (color != BoardColor.WHITE.value && color != BoardColor.BLACK.value)
            throw new IllegalArgumentException("invalid color");
        pieceMajor[color] = n;
    }

    /**
     * Getter for minor piece number.
     *
     * @param color
     * @return pieceMinor at index color
     * @throws IllegalArgumentException if invalid color
     */
    protected int getPieceNumMinor(int color) {
        if (color != BoardColor.WHITE.value && color != BoardColor.BLACK.value)
            throw new IllegalArgumentException("invalid color");
        return pieceMinor[color];
    }

    /**
     * Setter for minor piece number.
     *
     * @param n
     * @param color
     * @throws IllegalArgumentException if invalid color
     */
    protected void setPieceNumMinor(int n, int color) {
        if (color != BoardColor.WHITE.value && color != BoardColor.BLACK.value)
            throw new IllegalArgumentException("invalid color");
        pieceMinor[color] = n;
    }

    /**
     * Getter for material.
     *
     * @param color
     * @return material at index color
     * @throws IllegalArgumentException if invalid color
     */
    protected int getMaterial(int color) {
        if (color != BoardColor.WHITE.value && color != BoardColor.BLACK.value)
            throw new IllegalArgumentException("invalid color");
        return material[color];
    }

    /**
     * Setter for material.
     *
     * @param score
     * @param color
     * @throws IllegalArgumentException if invalid color
     */
    protected void setMaterial(int score, int color) {
        if (color != BoardColor.WHITE.value && color != BoardColor.BLACK.value)
            throw new IllegalArgumentException("invalid color");
        material[color] = score;
    }

    /**
     * Getter for castle permissions.
     *
     * @return castlePerm
     */
    protected int getCastlePerm() {
        return castlePerm;
    }

    /**
     * Setter for castle permissions.
     *
     * @param castlePerm
     */
    protected void setCastlePerm(int castlePerm) {
        this.castlePerm = castlePerm;
    }

    /**
     * Getter for history entry.
     *
     * @param i
     * @return UndoStructure entry at index i
     */
    protected UndoStructure getHistoryEntry(int i) {
        return history[i];
    }

    /**
     * Setter for history entry.
     *
     * @param entry
     * @param i
     */
    protected void setHistoryEntry(UndoStructure entry, int i) {
        history[i] = entry;
    }

    /**
     * Getter for piece list entry.
     *
     * @param x
     * @param y
     * @return piece list entry at index (x, y)
     */
    protected int getPieceListEntry(int x, int y) {
        return pieceList[x][y];
    }

    /**
     * Setter for piece list entry.
     *
     * @param piece
     * @param x
     * @param y
     */
    protected void setPieceListEntry(int piece, int x, int y) {
        pieceList[x][y] = piece;
    }

    /**
     * Getter for piece keys.
     *
     * @param x
     * @param y
     * @return piece key at index (x, y)
     */
    protected long getPieceKey(int x, int y) {
        return pieceKeys[x][y];
    }

    /**
     * Setter for piece keys.
     *
     * @param key
     * @param x
     * @param y
     */
    protected void setPieceKey(long key, int x, int y) {
        pieceKeys[x][y] = key;
    }

    /**
     * Getter for side key.
     *
     * @return sideKey
     */
    protected long getSideKey() {
        return sideKey;
    }

    /**
     * Setter for side key.
     *
     * @param sideKey
     */
    protected void setSideKey(long sideKey) {
        this.sideKey = sideKey;
    }

    /**
     * Getter for castle key.
     *
     * @param i
     * @return castle key at index i
     */
    protected long getCastleKey(int i) {
        return castleKeys[i];
    }

    /**
     * Setter for castle key.
     *
     * @param castleKey
     */
    protected void setCastleKey(long castleKey, int i) {
        castleKeys[i] = castleKey;
    }

    /**
     * Getter for file board entry.
     *
     * @param i
     * @return file board entry at index i
     */
    protected int getFileBoardEntry(int i) {
        if (i < 0 || i >= BoardConstants.BOARD_SQR_NUM)
            throw new IndexOutOfBoundsException("invalid index");
        return fileBoard[i];
    }

    /**
     * Getter for rank board entry.
     *
     * @param i
     * @return rank board entry at index i
     */
    protected int getRankBoardEntry(int i) {
        if (i < 0 || i >= BoardConstants.BOARD_SQR_NUM)
            throw new IndexOutOfBoundsException("invalid index");
        return rankBoard[i];
    }

    /**
     * Getter for PVTable.
     *
     * @return pvTable
     */
    protected PVTable getPVTable() {
        return pvTable;
    }

    /**
     * Getter for PV array entry.
     *
     * @param i
     * @return PV array entry at index i
     */
    protected int getPVArrayEntry(int i) {
        if (i < 0 || i >= BoardConstants.MAX_DEPTH)
            throw new IndexOutOfBoundsException("invalid index");
        return pvArray[i];
    }

    /**
     * Setter for PV array entry.
     *
     * @param move
     * @param i
     */
    protected void setPVArrayEntry(int move, int i) {
        if (i < 0 || i >= BoardConstants.MAX_DEPTH)
            throw new IndexOutOfBoundsException("invalid index");
        pvArray[i] = move;
    }

    /**
     * Getter for search history entry.
     *
     * @param x
     * @param y
     * @return search history entry at index (x, y)
     */
    protected int getSearchHistoryEntry(int x, int y) {
        return searchHistory[x][y];
    }

    /**
     * Setter for search history entry.
     *
     * @param entry
     * @param x
     * @param y
     */
    protected void setSearchHistoryEntry(int entry, int x, int y) {
        searchHistory[x][y] = entry;
    }

    /**
     * Getter for search killer entry.
     *
     * @param x
     * @param y
     * @return search killer entry at index (x, y)
     */
    protected int getSearchKillersEntry(int x, int y) {
        return searchKillers[x][y];
    }

    /**
     * Setter for search killer entry.
     *
     * @param entry
     * @param x
     * @param y
     */
    protected void setSearchKillerEntry(int entry, int x, int y) {
        searchKillers[x][y] = entry;
    }

    /**
     * Initializes the array mappings from the size 120 board to
     * the size 64 board.
     */
    private void initSqr120AndSqr64() {
        int sqr;
        int sqr64 = 0;

        for (int i = 0; i < BoardConstants.BOARD_SQR_NUM; i++)
            sqr120ToSqr64[i] = 65;

        for (int r = BoardRank.RANK_1.value; r <= BoardRank.RANK_8.value; r++) {
            for (int f = BoardFile.FILE_A.value; f <= BoardFile.FILE_H.value; f++) {
                sqr = BoardUtils.convertFileRankToSqr(f, r);
                sqr64ToSqr120[sqr64] = sqr;
                sqr120ToSqr64[sqr] = sqr64;
                sqr64++;
            }
        }
    }

    /**
     * Initialize bit masks.
     */
    private void initBitMasks() {
        for (int i = 0; i < 64; i++) {
            setMask[i] = 1L << i;
            clearMask[i] = ~setMask[i];
        }
    }

    /**
     * Initializes hash keys.
     */
    private void initHashKeys() {
        Random random = new Random();

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 120; j++) {
                pieceKeys[i][j] = random.nextLong();
            }
        }

        sideKey = random.nextLong();

        for (int i = 0; i < 16; i++) {
            castleKeys[i] = random.nextLong();
        }
    }

    /**
     * Initializes file and rank board.
     */
    private void initFileAndRankBoard() {
        int sqr = BoardSquare.A1.value;
        for (int i = 0; i < BoardConstants.BOARD_SQR_NUM; i++) {
            this.fileBoard[i] = BoardSquare.OFFBOARD.value;
            this.rankBoard[i] = BoardSquare.OFFBOARD.value;
        }

        for (int r = BoardRank.RANK_1.value; r <= BoardRank.RANK_8.value; r++) {
            for (int f = BoardFile.FILE_A.value; f <= BoardFile.FILE_H.value; f++) {
                sqr = BoardUtils.convertFileRankToSqr(f, r);
                this.fileBoard[sqr] = f;
                this.rankBoard[sqr] = r;
            }
        }
    }

    /**
     * Initializes history.
     */
    private void initHistory() {
        for (int i = 0; i < BoardConstants.MAX_GAME_MOVES; i++)
            history[i] = new UndoStructure();
    }

    /**
     * Get index from size 120 board to size 64 board.
     *
     * @param sqr120
     */
    protected int sqr64(int sqr120) {
        return sqr120ToSqr64[sqr120];
    }

    /**
     * Get index from size 64 board to size 120 board.
     *
     * @param sqr64
     */
    protected int sqr120(int sqr64) {
        return sqr64ToSqr120[sqr64];
    }

    /**
     * Clears the square from the bitboard.
     *
     * @param bitboard
     * @param sqr
     * @return bitboard
     */
    protected long clearBit(long bitboard, int sqr) {
        return bitboard & clearMask[sqr];
    }

    /**
     * Sets the square on the bitboard.
     *
     * @param bitboard
     * @param sqr
     * @return bitboard
     */
    protected long setBit(long bitboard, int sqr) {
        return bitboard | setMask[sqr];
    }

    /**
     * Resets the board structure.
     */
    protected void resetBoard() {
        for (int i = 0; i < BoardConstants.BOARD_SQR_NUM; i++)
            pieces[i] = BoardSquare.OFFBOARD.value;

        for (int i = 0; i < 64; i++)
            pieces[sqr120(i)] = BoardPiece.EMPTY.value;

        for (int i = 0; i < 2; i++) {
            pieceBig[i] = 0;
            pieceMajor[i] = 0;
            pieceMinor[i] = 0;
            material[i] = 0;
        }

        for (int i = 0; i < 3; i++)
            pawns[i] = 0L;

        for (int i = 0; i < 13; i++)
            pieceNum[i] = 0;

        kingSqr[BoardColor.WHITE.value] = BoardSquare.NONE.value;
        kingSqr[BoardColor.BLACK.value] = BoardSquare.NONE.value;
        side = BoardColor.BOTH.value;
        enPassant = BoardSquare.NONE.value;
        fiftyMove = 0;
        ply = 0;
        historyPly = 0;
        castlePerm = 0;
        positionKey = 0L;
        pvTable = new PVTable();
        pvTable.initPVTable();
    }

    /**
     * Parses a board position in Forsyth-Edwards Notation (FEN).
     *
     * @param fen
     * @return 0
     */
    protected int parseFEN(String fen) {
        int rank = BoardRank.RANK_8.value;
        int file = BoardFile.FILE_A.value;
        int piece = 0;
        int count = 0;
        int sqr64 = 0;
        int sqr120 = 0;
        int ptr = 0;
        char currentChar = ' ';

        resetBoard();

        while ((rank >= BoardRank.RANK_1.value) && ptr < fen.length()) {
            count = 1;
            currentChar = fen.charAt(ptr);

            switch (currentChar) {
                case 'p':
                    piece = BoardPiece.BLACK_PAWN.value;
                    break;
                case 'r':
                    piece = BoardPiece.BLACK_ROOK.value;
                    break;
                case 'n':
                    piece = BoardPiece.BLACK_KNIGHT.value;
                    break;
                case 'b':
                    piece = BoardPiece.BLACK_BISHOP.value;
                    break;
                case 'k':
                    piece = BoardPiece.BLACK_KING.value;
                    break;
                case 'q':
                    piece = BoardPiece.BLACK_QUEEN.value;
                    break;
                case 'P':
                    piece = BoardPiece.WHITE_PAWN.value;
                    break;
                case 'R':
                    piece = BoardPiece.WHITE_ROOK.value;
                    break;
                case 'N':
                    piece = BoardPiece.WHITE_KNIGHT.value;
                    break;
                case 'B':
                    piece = BoardPiece.WHITE_BISHOP.value;
                    break;
                case 'K':
                    piece = BoardPiece.WHITE_KING.value;
                    break;
                case 'Q':
                    piece = BoardPiece.WHITE_QUEEN.value;
                    break;

                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                    piece = BoardPiece.EMPTY.value;
                    count = currentChar - '0';
                    break;

                case '/':
                case ' ':
                    rank--;
                    file = BoardFile.FILE_A.value;
                    ptr++;
                    continue;

                default:
                    System.out.println("Error with FEN.");
                    break;
            }

            for (int i = 0; i < count; i++) {
                sqr64 = rank * 8 + file;
                sqr120 = sqr120(sqr64);
                if (piece != BoardPiece.EMPTY.value)
                    pieces[sqr120] = piece;
                file++;
            }

            ptr++;
        }

        currentChar = fen.charAt(ptr);

        side = (currentChar == 'w') ? BoardColor.WHITE.value : BoardColor.BLACK.value;
        ptr += 2;

        for (int i = 0; i < 4; i++) {
            currentChar = fen.charAt(ptr);

            if (currentChar == ' ')
                break;

            switch(currentChar) {
                case 'K':
                    this.castlePerm |= BoardCastle.WHITE_KING_CASTLE.value;
                    break;
                case 'Q':
                    this.castlePerm |= BoardCastle.WHITE_QUEEN_CASTLE.value;
                    break;
                case 'k':
                    this.castlePerm |= BoardCastle.BLACK_KING_CASTLE.value;
                    break;
                case 'q':
                    this.castlePerm |= BoardCastle.BLACK_QUEEN_CASTLE.value;
                    break;
                default:
                    break;
            }
            ptr++;
        }
        ptr++;

        currentChar = fen.charAt(ptr);
        if (currentChar != '-') {
            file = fen.charAt(ptr) - 'a';
            rank = fen.charAt(ptr+1) - '1';
            enPassant = BoardUtils.convertFileRankToSqr(file, rank);
        }

        positionKey = PositionKey.generatePositionKey(this);
        return 0;
    }

    /**
     * Prints the state of the board.
     */
    protected void printBoard() {
        int sqr;
        int piece;

        System.out.println("Board State:\n");

        for (int r = BoardRank.RANK_8.value; r >= BoardRank.RANK_1.value; r--) {
            System.out.print((r+1) + "  ");
            for (int f = BoardFile.FILE_A.value; f <= BoardFile.FILE_H.value; f++) {
                sqr = BoardUtils.convertFileRankToSqr(f, r);
                piece = pieces[sqr];
                System.out.print(BoardUtils.getPieceChar(piece) + " ");
            }
            System.out.println();
        }

        System.out.print("\n   ");
        for (int f = BoardFile.FILE_A.value; f <= BoardFile.FILE_H.value; f++)
            System.out.print((char) (f + 'a') + " ");

        System.out.println("\n");

        System.out.println("        Side: " + BoardUtils.getSideChar(side));
        System.out.println("  En Passant: " + enPassant);
        System.out.println("      Castle: " +
                ((castlePerm & BoardCastle.WHITE_KING_CASTLE.value)  != 0 ? "K" : "-") +
                ((castlePerm & BoardCastle.WHITE_QUEEN_CASTLE.value) != 0 ? "Q" : "-") +
                ((castlePerm & BoardCastle.BLACK_KING_CASTLE.value)  != 0 ? "k" : "-") +
                ((castlePerm & BoardCastle.BLACK_QUEEN_CASTLE.value) != 0 ? "q" : "-"));
        System.out.println("Position Key: " + positionKey);
    }

    /**
     * Updates list materials.
     */
    protected void updateListMaterials() {
        int piece = 0;
        int sqr = 0;
        int color = 0;
        for (int i = 0; i < BoardConstants.BOARD_SQR_NUM; i++) {
            sqr = i;
            piece = pieces[i];

            if (piece != BoardSquare.OFFBOARD.value &&
                piece != BoardPiece.EMPTY.value) {
                color = BoardUtils.getPieceColor(piece);
                if (BoardUtils.isPieceBig(piece))
                    pieceBig[color]++;
                if (BoardUtils.isPieceMajor(piece))
                    pieceMajor[color]++;
                if (BoardUtils.isPieceMinor(piece))
                    pieceMinor[color]++;

                material[color] += BoardUtils.getPieceValue(piece);
                pieceList[piece][pieceNum[piece]] = sqr;
                pieceNum[piece]++;

                if (piece == BoardPiece.WHITE_KING.value) {
                    kingSqr[BoardColor.WHITE.value] = sqr;
                } else if (piece == BoardPiece.BLACK_KING.value) {
                    kingSqr[BoardColor.BLACK.value] = sqr;
                }

                if (piece == BoardPiece.WHITE_PAWN.value) {
                    pawns[BoardColor.WHITE.value] =
                            setBit(pawns[BoardColor.WHITE.value], sqr64(sqr));
                    pawns[BoardColor.BOTH.value] =
                            setBit(pawns[BoardColor.BOTH.value], sqr64(sqr));
                } else if (piece == BoardPiece.BLACK_PAWN.value) {
                    pawns[BoardColor.BLACK.value] =
                            setBit(pawns[BoardColor.BLACK.value], sqr64(sqr));
                    pawns[BoardColor.BOTH.value] =
                            setBit(pawns[BoardColor.BOTH.value], sqr64(sqr));
                }
            }
        }
    }

    /**
     * Prints out the move.
     *
     * @param move
     * @return moveString
     */
    protected String printMove(int move) {
        String moveString = "";

        int fileFrom = fileBoard[MoveUtils.from(move)];
        int rankFrom = rankBoard[MoveUtils.from(move)];
        int fileTo = fileBoard[MoveUtils.to(move)];
        int rankTo = rankBoard[MoveUtils.to(move)];
        int promoted = MoveUtils.promoted(move);

        if (promoted != 0) {
            char pChar = 'q';
            if (BoardUtils.isPieceKnight(promoted)) {
                pChar = 'n';
            } else if (BoardUtils.isPieceRookOrQueen(promoted) &&
                    !BoardUtils.isPieceBishopOrQueen(promoted)) {
                pChar = 'r';
            } else if (!BoardUtils.isPieceRookOrQueen(promoted) &&
                    BoardUtils.isPieceBishopOrQueen(promoted)) {
                pChar = 'b';
            }
            moveString = new String(new char[]{
                    (char)('a' + fileFrom),
                    (char)('1' + rankFrom),
                    (char)('a' + fileTo),
                    (char)('1' + rankTo),
                    pChar
            });
        } else {
            moveString = new String(new char[]{
                    (char)('a' + fileFrom),
                    (char)('1' + rankFrom),
                    (char)('a' + fileTo),
                    (char)('1' + rankTo)
            });
        }
        return moveString;
    }
}
