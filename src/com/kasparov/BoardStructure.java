package com.kasparov;

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
    int[] pieces = new int[BoardConstants.BOARD_SQR_NUM];

    /**
     * 64 bits represents 8 x 8 board, where 1 denotes pawn at that
     * location, and 0 denotes no pawn at that location.
     */
    long[] pawns = new long[3];

    /**
     * Locations of the two kings.
     */
    int[] kingSqr = new int[2];

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
    int[] pieceBig = new int[2];

    /**
     * Number of major pieces (queens and rooks), by color
     * (white, black, both).
     */
    int[] pieceMajor = new int[2];

    /**
     * Number of minor pieces (bishops and knights), by color
     * (white, black, both).
     */
    int[] pieceMinor = new int[2];

    /**
     * Material.
     */
    int[] material = new int[2];

    /**
     * Castle permissions.
     */
    int castlePerm;

    /**
     * Keeps track of the history of the game.
     */
    UndoStructure[] history = new UndoStructure[BoardConstants.MAX_GAME_MOVES];

    /**
     * Maps square index from size 120 board to size 64 board.
     */
    int[] sqr120ToSqr64 = new int[BoardConstants.BOARD_SQR_NUM];

    /**
     * Maps square index from size 64 board to size 120 board.
     */
    int[] sqr64ToSqr120 = new int[64];

    /**
     * Piece list.
     */
    int[][] pieceList = new int[13][10];

    /**
     * Bit table.
     */
    int[] bitTable = {
        63, 30,  3, 32, 25, 41, 22, 33,
        15, 50, 42, 13, 11, 53, 19, 34,
        61, 29,  2, 51, 21, 43, 45, 10,
        18, 47,  1, 54,  9, 57,  0, 35,
        62, 31, 40,  4, 49,  5, 52, 26,
        60,  6, 23, 44, 46, 27, 56, 16,
         7, 39, 48, 24, 59, 14, 12, 55,
        38, 28, 58, 20, 37, 17, 36,  8
    };

    /**
     * Set mask.
     */
    long[] setMask = new long[64];

    /**
     * Clear mask.
     */
    long[] clearMask = new long[64];

    /**
     * Piece keys.
     */
    long[][] pieceKeys = new long[13][120];

    /**
     * Side key.
     */
    long sideKey;

    /**
     * Castle keys.
     */
    long[] castleKeys = new long[16];

    /**
     * Files board.
     */
    int[] fileBoard = new int[BoardConstants.BOARD_SQR_NUM];

    /**
     * Ranks board.
     */
    int[] rankBoard = new int[BoardConstants.BOARD_SQR_NUM];

    /**
     * Pieces check.
     */
    int[] piecesKnight = new int[13];
    int[] piecesKing = new int[13];
    int[] piecesRookOrQueen = new int[13];
    int[] piecesBishopOrQueen = new int[13];

    /**
     * Principle variation table.
     */
    PVTable pvTable;
    int[] pvArray = new int[BoardConstants.MAX_DEPTH];

    /**
     * Search history.
     */
    int[][] searchHistory = new int[13][BoardConstants.BOARD_SQR_NUM];
    int[][] searchKillers = new int[2][BoardConstants.MAX_DEPTH];


    /**
     * Initializes an empty BoardStructure.
     */
    public BoardStructure() {}

    /**
     * Initializes the array mappings from the size 120 board to
     * the size 64 board.
     */
    public void initSqr120AndSqr64() {
        int sqr;
        int sqr64 = 0;

        for (int i = 0; i < BoardConstants.BOARD_SQR_NUM; i++)
            this.sqr120ToSqr64[i] = 65;

        for (int r = BoardRank.RANK_1.value; r <= BoardRank.RANK_8.value; r++) {
            for (int f = BoardFile.FILE_A.value; f <= BoardFile.FILE_H.value; f++) {
                sqr = BoardConstants.convertFileRankToSqr(f, r);
                sqr64ToSqr120[sqr64] = sqr;
                sqr120ToSqr64[sqr] = sqr64;
                sqr64++;
            }
        }
    }

    /**
     * Prints the size 120 board.
     *
     * @param hideOuter
     */
    public void printSqr120(boolean hideOuter) {
        for (int i = 0; i < BoardConstants.BOARD_SQR_NUM; i++) {
            if (i % 10 == 0)
                System.out.println();
            if (this.sqr120ToSqr64[i] < 10)
                System.out.print(" ");
            if (hideOuter && this.sqr120ToSqr64[i] == 65)
                System.out.print(" * ");
            else
                System.out.print(this.sqr120ToSqr64[i] + " ");
        }
        System.out.println();
    }

    /**
     * Prints the size 64 board.
     */
    public void printSqr64() {
        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0)
                System.out.println();
            System.out.print(this.sqr64ToSqr120[i] + " ");
        }
        System.out.println();
    }

    /**
     * Prints the bitboard.
     *
     * @param bitboard
     */
    public void printBitBoard(long bitboard) {

        int sqr;
        int sqr64;

        System.out.println();
        for (int r = BoardRank.RANK_8.value; r >= BoardRank.RANK_1.value; r--) {
            for (int f = BoardFile.FILE_A.value; f <= BoardFile.FILE_H.value; f++) {
                sqr = BoardConstants.convertFileRankToSqr(f, r);
                sqr64 = this.sqr120ToSqr64[sqr];

                if (((1L << sqr64) & bitboard) != 0)
                    System.out.print("X ");
                else
                    System.out.print("- ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Pops a bit from the bitboard.
     *
     * @param bitboard
     */
    public long popBit(long bitboard) {
        return bitboard & (bitboard - 1);
    }

    /**
     * Counts the number of 1 bits in the bitboard.
     *
     * @param bitboard
     */
    public int countBits(long bitboard) {
        int r;
        for (r = 0; bitboard != 0; r++, bitboard &= bitboard - 1);
        return r;
    }

    /**
     * Get index from size 120 board to size 64 board.
     *
     * @param sqr120
     */
    public int sqr64(int sqr120) {
        return this.sqr120ToSqr64[sqr120];
    }

    /**
     * Get index from size 64 board to size 120 board.
     *
     * @param sqr64
     */
    public int sqr120(int sqr64) {
        return this.sqr64ToSqr120[sqr64];
    }

    /**
     * Initialize bit masks.
     */
    public void initBitMasks() {
        for (int i = 0; i < 64; i++) {
            setMask[i] = 1L << i;
            clearMask[i] = ~setMask[i];
        }
    }

    /**
     * Clear bit.
     */
    public long clearBit(long bitboard, int sqr) {
        return bitboard & clearMask[sqr];
    }

    /**
     * Set bit.
     */
    public long setBit(long bitboard, int sqr) {
        return bitboard | setMask[sqr];
    }

    /**
     * Initializes history.
     */
    public void initHistory() {
        for (int i = 0; i < BoardConstants.MAX_GAME_MOVES; i++)
            history[i] = new UndoStructure();
    }

    /**
     * Initializes hash keys.
     */
    public void initHashKeys() {
        Random random = new Random();

        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 120; j++) {
                this.pieceKeys[i][j] = random.nextLong();
            }
        }

        this.sideKey = random.nextLong();

        for (int i = 0; i < 16; i++) {
            castleKeys[i] = random.nextLong();
        }
    }

    /**
     * Resets the board structure.
     */
    public void resetBoard() {
        int i;
        for (i = 0; i < BoardConstants.BOARD_SQR_NUM; i++)
            this.pieces[i] = BoardSquare.OFFBOARD.value;

        for (i = 0; i < 64; i++)
            this.pieces[sqr120(i)] = BoardPiece.EMPTY.value;

        for (i = 0; i < 2; i++) {
            this.pieceBig[i] = 0;
            this.pieceMajor[i] = 0;
            this.pieceMinor[i] = 0;
            this.material[i] = 0;
        }

        for (i = 0; i < 3; i++) {
            this.pawns[i] = 0L;
        }

        for (i = 0; i < 13; i++) {
            this.pieceNum[i] = 0;
        }

        this.kingSqr[BoardColor.WHITE.value] = BoardSquare.NONE.value;
        this.kingSqr[BoardColor.BLACK.value] = BoardSquare.NONE.value;

        this.side = BoardColor.BOTH.value;
        this.enPassant = BoardSquare.NONE.value;
        this.fiftyMove = 0;

        this.ply = 0;
        this.historyPly = 0;

        this.castlePerm = 0;
        this.positionKey = 0L;

        this.pvTable = new PVTable();
        this.pvTable.initPVTable();
    }

    /**
     * Parses a board position in Forsyth-Edwards Notation (FEN).
     */
    public int parseFEN(String fen) {

        int rank = BoardRank.RANK_8.value;
        int file = BoardFile.FILE_A.value;
        int piece = 0;
        int count = 0;
        int sqr64 = 0;
        int sqr120 = 0;
        int ptr = 0;
        char currentChar = ' ';

        this.resetBoard();

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
                    this.pieces[sqr120] = piece;
                file++;
            }

            ptr++;
        }

        currentChar = fen.charAt(ptr);
        assert (currentChar == 'w' || currentChar == 'b');

        this.side = (currentChar == 'w') ? BoardColor.WHITE.value : BoardColor.BLACK.value;
        ptr += 2;

        for (int i = 0; i < 4; i++) {
            currentChar = fen.charAt(ptr);

            if (currentChar == ' ')
                break;

            switch(currentChar) {
                case 'K':
                    this.castlePerm |= BoardCastleLink.WHITE_KING_CASTLE.value;
                    break;
                case 'Q':
                    this.castlePerm |= BoardCastleLink.WHITE_QUEEN_CASTLE.value;
                    break;
                case 'k':
                    this.castlePerm |= BoardCastleLink.BLACK_KING_CASTLE.value;
                    break;
                case 'q':
                    this.castlePerm |= BoardCastleLink.BLACK_QUEEN_CASTLE.value;
                    break;
                default:
                    break;
            }
            ptr++;
        }
        ptr++;

        assert (this.castlePerm >= 0 && this.castlePerm <= 15);

        currentChar = fen.charAt(ptr);
        if (currentChar != '-') {
            file = fen.charAt(ptr) - 'a';
            rank = fen.charAt(ptr+1) - '1';

            assert (file >= BoardFile.FILE_A.value && file <= BoardFile.FILE_H.value);
            assert (rank >= BoardRank.RANK_1.value && rank <= BoardRank.RANK_8.value);

            this.enPassant = BoardConstants.convertFileRankToSqr(file, rank);
        }

        this.positionKey = PositionKey.generatePositionKey(this);
        return 0;
    }

    /**
     * Prints the state of the board.
     */
    public void printBoard() {

        int sqr;
        int piece;

        System.out.println("Board State:\n");

        for (int r = BoardRank.RANK_8.value; r >= BoardRank.RANK_1.value; r--) {
            System.out.print((r+1) + "  ");
            for (int f = BoardFile.FILE_A.value; f <= BoardFile.FILE_H.value; f++) {
                sqr = BoardConstants.convertFileRankToSqr(f, r);
                piece = this.pieces[sqr];
                System.out.print(BoardConstants.pieceChars.charAt(piece) + " ");
            }
            System.out.println();
        }

        System.out.print("\n   ");
        for (int f = BoardFile.FILE_A.value; f <= BoardFile.FILE_H.value; f++)
            System.out.print((char) (f + 'a') + " ");

        System.out.println("\n");

        System.out.println("        Side: " + BoardConstants.sideChars.charAt(this.side));
        System.out.println("  En Passant: " + this.enPassant);
        System.out.println("      Castle: " +
                ((this.castlePerm & BoardCastleLink.WHITE_KING_CASTLE.value)  != 0 ? "K" : "-") +
                ((this.castlePerm & BoardCastleLink.WHITE_QUEEN_CASTLE.value) != 0 ? "Q" : "-") +
                ((this.castlePerm & BoardCastleLink.BLACK_KING_CASTLE.value)  != 0 ? "k" : "-") +
                ((this.castlePerm & BoardCastleLink.BLACK_QUEEN_CASTLE.value) != 0 ? "q" : "-"));
        System.out.println("Position Key: " + this.positionKey);
    }

    /**
     * Updates list materials.
     */
    public void updateListMaterials() {
        int piece = 0;
        int sqr = 0;
        int color = 0;
        for (int i = 0; i < BoardConstants.BOARD_SQR_NUM; i++) {
            sqr = i;
            piece = this.pieces[i];

            if (piece != BoardSquare.OFFBOARD.value &&
                piece != BoardPiece.EMPTY.value) {
                color = BoardConstants.pieceColor[piece];
                if (BoardConstants.pieceBig[piece])
                    this.pieceBig[color]++;
                if (BoardConstants.pieceMajor[piece])
                    this.pieceMajor[color]++;
                if (BoardConstants.pieceMinor[piece])
                    this.pieceMinor[color]++;

                this.material[color] += BoardConstants.pieceValue[piece];
                this.pieceList[piece][this.pieceNum[piece]] = sqr;
                this.pieceNum[piece]++;

                if (piece == BoardPiece.WHITE_KING.value) {
                    this.kingSqr[BoardColor.WHITE.value] = sqr;
                } else if (piece == BoardPiece.BLACK_KING.value) {
                    this.kingSqr[BoardColor.BLACK.value] = sqr;
                }

                if (piece == BoardPiece.WHITE_PAWN.value) {
                    this.pawns[BoardColor.WHITE.value] =
                            this.setBit(this.pawns[BoardColor.WHITE.value], sqr64(sqr));
                    this.pawns[BoardColor.BOTH.value] =
                            this.setBit(this.pawns[BoardColor.BOTH.value], sqr64(sqr));
                } else if (piece == BoardPiece.BLACK_PAWN.value) {
                    this.pawns[BoardColor.BLACK.value] =
                            this.setBit(this.pawns[BoardColor.BLACK.value], sqr64(sqr));
                    this.pawns[BoardColor.BOTH.value] =
                            this.setBit(this.pawns[BoardColor.BOTH.value], sqr64(sqr));
                }
            }
        }
    }

    /**
     * Initializes file and rank board.
     */
    public void initFileAndRankBoard() {
        int file = BoardFile.FILE_A.value;
        int rank = BoardRank.RANK_1.value;
        int sqr = BoardSquare.A1.value;
        int sqr64 = 0;

        for (int i = 0; i < BoardConstants.BOARD_SQR_NUM; i++) {
            this.fileBoard[i] = BoardSquare.OFFBOARD.value;
            this.rankBoard[i] = BoardSquare.OFFBOARD.value;
        }

        for (int r = BoardRank.RANK_1.value; r <= BoardRank.RANK_8.value; r++) {
            for (int f = BoardFile.FILE_A.value; f <= BoardFile.FILE_H.value; f++) {
                sqr = BoardConstants.convertFileRankToSqr(f, r);
                this.fileBoard[sqr] = f;
                this.rankBoard[sqr] = r;
            }
        }
    }

    /**
     * Prints the file board.
     */
    public void printFileBoard() {
        System.out.println("\nFiles Board:\n");
        for (int i = 0; i < BoardConstants.BOARD_SQR_NUM; i++) {
            if (i % 10 == 0 && i != 0)
                System.out.println();
            System.out.printf("%4d", this.fileBoard[i]);
        }
        System.out.println();
    }

    /**
     * Prints the rank board.
     */
    public void printRankBoard() {
        System.out.println("\nRanks Board:\n");
        for (int i = 0; i < BoardConstants.BOARD_SQR_NUM; i++) {
            if (i % 10 == 0 && i != 0)
                System.out.println();
            System.out.printf("%4d", this.rankBoard[i]);
        }
        System.out.println();
    }

    /**
     * Checkboard.
     */
    public boolean checkBoard() {

        int[] tempPieceNum = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] tempPieceBig = {0, 0};
        int[] tempPieceMajor = {0, 0};
        int[] tempPieceMinor = {0, 0};
        int[] tempMaterial = {0, 0};

        int tempPiece;
        int sqr120;
        int color;
        int pCount;

        long[] tempPawns = {0L, 0L, 0L};

        tempPawns[BoardColor.WHITE.value] = this.pawns[BoardColor.WHITE.value];
        tempPawns[BoardColor.BLACK.value] = this.pawns[BoardColor.BLACK.value];
        tempPawns[BoardColor.BOTH.value] = this.pawns[BoardColor.BOTH.value];

        for (tempPiece = BoardPiece.WHITE_PAWN.value; tempPiece <= BoardPiece.BLACK_KING.value; tempPiece++) {
            for (int num = 0; num < this.pieceNum[tempPiece]; num++) {
                sqr120 = this.pieceList[tempPiece][num];
                assert (tempPiece == this.pieces[sqr120]);
            }
        }

        for (int sqr64 = 0; sqr64 < 64; sqr64++) {
            sqr120 = sqr120(sqr64);
            tempPiece = this.pieces[sqr120];
            tempPieceNum[tempPiece]++;
            color = BoardConstants.pieceColor[tempPiece];

            if (BoardConstants.pieceBig[tempPiece])
                tempPieceBig[color]++;
            if (BoardConstants.pieceMajor[tempPiece])
                tempPieceMajor[color]++;
            if (BoardConstants.pieceMinor[tempPiece])
                tempPieceMinor[color]++;
        }

        for (tempPiece = BoardPiece.WHITE_PAWN.value; tempPiece <= BoardPiece.BLACK_KING.value; tempPiece++) {
            assert (tempPieceNum[tempPiece] == this.pieceNum[tempPiece]);
        }

        assert((tempPieceMajor[BoardColor.WHITE.value] == this.pieceMajor[BoardColor.WHITE.value]) &&
               (tempPieceMajor[BoardColor.BLACK.value] == this.pieceMajor[BoardColor.BLACK.value]));
        assert((tempPieceMinor[BoardColor.WHITE.value] == this.pieceMinor[BoardColor.WHITE.value]) &&
               (tempPieceMinor[BoardColor.BLACK.value] == this.pieceMinor[BoardColor.BLACK.value]));
        assert((tempPieceBig[BoardColor.WHITE.value] == this.pieceBig[BoardColor.WHITE.value]) &&
               (tempPieceBig[BoardColor.BLACK.value] == this.pieceBig[BoardColor.BLACK.value]));
        assert (this.side == BoardColor.WHITE.value ||
                this.side == BoardColor.BLACK.value);
        assert (PositionKey.generatePositionKey(this) == this.positionKey);

        assert (this.enPassant == BoardSquare.NONE.value ||
                (this.rankBoard[this.enPassant] == BoardRank.RANK_6.value &&
                 this.side == BoardColor.WHITE.value) ||
                (this.rankBoard[this.enPassant] == BoardRank.RANK_3.value &&
                 this.side == BoardColor.BLACK.value));

        assert (this.pieces[this.kingSqr[BoardColor.WHITE.value]] == BoardPiece.WHITE_KING.value);
        assert (this.pieces[this.kingSqr[BoardColor.BLACK.value]] == BoardPiece.BLACK_KING.value);

        return true;
    }

    /**
     * Prints out a square.
     */
    public void printSqr(int sqr) {
        String sqrStr = "";

        int file = this.fileBoard[sqr];
        int rank = this.rankBoard[sqr];

        sqrStr = new String(new char[] {
                (char)('a' + file),
                (char)('1' + rank)
        });

        System.out.println(sqrStr);
    }

    /**
     * Prints out the move.
     */
    public String printMove(int move) {
        String moveString = "";

        int fileFrom = this.fileBoard[Move.from(move)];
        int rankFrom = this.rankBoard[Move.from(move)];
        int fileTo = this.fileBoard[Move.to(move)];
        int rankTo = this.rankBoard[Move.to(move)];
        int promoted = Move.promoted(move);

        if (promoted != 0) {
            char pChar = 'q';
            if (BoardConstants.isKnight(promoted)) {
                pChar = 'n';
            } else if (BoardConstants.isRookOrQueen(promoted) &&
                    !BoardConstants.isBishopOrQueen(promoted)) {
                pChar = 'r';
            } else if (!BoardConstants.isRookOrQueen(promoted) &&
                    BoardConstants.isBishopOrQueen(promoted)) {
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

    /**
     * Initializes the PVTable.
     */
    public void initPVTable() {
        this.pvTable.initPVTable();
    }

}
