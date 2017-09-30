package com.kasparov;

import javafx.geometry.Pos;

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
     * castleKeys.
     */
    long[] castleKeys = new long[16];


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

        for (i = 0; i < 3; i++) {
            this.pieceBig[i] = 0;
            this.pieceMajor[i] = 0;
            this.pieceMinor[i] = 0;
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
                // System.out.println(sqr64);
                sqr120 = sqr120(sqr64);
                if (piece != BoardPiece.EMPTY.value)
                    this.pieces[sqr120] = piece;
                file++;
            }

            ptr++;
        }

        currentChar = fen.charAt(ptr);
        // System.out.println(currentChar == 'w' || currentChar == 'b');

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
                    this.castlePerm |= BoardCastleLink.WHITE_QUEEN_CASTLE.value;
                    break;
                default:
                    break;
            }
            ptr++;
        }
        ptr++;

        // System.out.println(this.castlePerm >= 0 && this.castlePerm <= 15);

        currentChar = fen.charAt(ptr);
        if (currentChar != '-') {
            file = fen.charAt(ptr) - 'a';
            rank = fen.charAt(ptr+1) - '1';

            // System.out.println(file >= BoardFile.FILE_A.value && file <= BoardFile.FILE_H.value);
            // System.out.println(rank >= BoardRank.RANK_1.value && rank <= BoardRank.RANK_8.value);

            this.enPassant = BoardConstants.convertFileRankToSqr(file, rank);
        }

        this.positionKey = PositionKey.generatePositionKey(this);

        System.out.println(positionKey);

        return 0;
    }

}
