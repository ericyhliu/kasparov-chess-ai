package com.kasparov;

public class MakeMove {

    static void hashPiece(BoardStructure boardStructure, int piece, int sqr) {
        boardStructure.positionKey ^= boardStructure.pieceKeys[piece][sqr];
    }

    static long hashCastle(BoardStructure boardStructure) {
        return boardStructure.positionKey ^ boardStructure.castleKeys[boardStructure.castlePerm];
    }

    static long hashSide(BoardStructure boardStructure) {
        return boardStructure.positionKey ^ boardStructure.sideKey;
    }

    static long hashEnPassant(BoardStructure boardStructure) {
        return boardStructure.positionKey ^ boardStructure.pieceKeys[BoardPiece.EMPTY.value][boardStructure.enPassant];
    }

    static int[] castlePerm = {
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 13, 15, 15, 15, 12, 15, 15, 14, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15,  7, 15, 15, 15,  3, 15, 15, 11, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15,
        15, 15, 15, 15, 15, 15, 15, 15, 15, 15
    };

    static void clearPiece(BoardStructure boardStructure, int sqr) {
        int piece = boardStructure.pieces[sqr];
        int col = BoardConstants.pieceColor[piece];
        int tempPieceNum = -1;

        hashPiece(boardStructure, piece, sqr);

        boardStructure.pieces[sqr] = BoardPiece.EMPTY.value;
        boardStructure.material[col] -= BoardConstants.pieceValue[piece];

        if (BoardConstants.pieceBig[piece]) {
            boardStructure.pieceBig[col]--;

            if (BoardConstants.pieceMajor[piece]) {
                boardStructure.pieceMajor[col]--;
            } else {
                boardStructure.pieceMinor[col]--;
            }
        } else {
            boardStructure.clearBit(boardStructure.pawns[col], boardStructure.sqr64(sqr));
            boardStructure.clearBit(boardStructure.pawns[BoardColor.BOTH.value], boardStructure.sqr64(sqr));
        }

        for (int i = 0; i < boardStructure.pieceNum[piece]; i++) {
            if (boardStructure.pieceList[piece][i] == sqr) {
                tempPieceNum = i;
                break;
            }
        }

        boardStructure.pieceNum[piece]--;
        boardStructure.pieceList[piece][tempPieceNum] = boardStructure.pieceList[piece][boardStructure.pieceNum[piece]];
    }

    static void addPiece(BoardStructure boardStructure, int sqr, int piece) {
        int col = BoardConstants.pieceColor[piece];
        hashPiece(boardStructure, piece, sqr);

        boardStructure.pieces[sqr] = piece;

        if (BoardConstants.pieceBig[piece]) {
            boardStructure.pieceBig[col]++;
            if (BoardConstants.pieceMajor[piece]) {
                boardStructure.pieceMajor[col]++;
            } else {
                boardStructure.pieceMinor[col]++;
            }
        } else {
            boardStructure.setBit(boardStructure.pawns[col], boardStructure.sqr64(sqr));
            boardStructure.setBit(boardStructure.pawns[BoardColor.BOTH.value], boardStructure.sqr64(sqr));
        }

        boardStructure.material[col] += BoardConstants.pieceValue[piece];
        boardStructure.pieceList[piece][boardStructure.pieceNum[piece]++] = sqr;
    }

    static void movePiece(BoardStructure boardStructure, int from, int to) {
        int piece = boardStructure.pieces[from];
        int col = BoardConstants.pieceColor[piece];
        boolean tempPieceNum = false;

        hashPiece(boardStructure, piece, from);

        boardStructure.pieces[from] = BoardPiece.EMPTY.value;

        hashPiece(boardStructure, piece, to);

        boardStructure.pieces[to] = piece;

        if (!BoardConstants.pieceBig[piece]) {
            boardStructure.clearBit(boardStructure.pawns[col], boardStructure.sqr64(from));
            boardStructure.clearBit(boardStructure.pawns[BoardColor.BOTH.value], boardStructure.sqr64(from));
            boardStructure.setBit(boardStructure.pawns[col], boardStructure.sqr64(to));
            boardStructure.setBit(boardStructure.pawns[BoardColor.BOTH.value], boardStructure.sqr64(to));
        }

        for (int i = 0; i < boardStructure.pieceNum[piece]; i++) {
            if (boardStructure.pieceList[piece][i] == from) {
                boardStructure.pieceList[piece][i] = to;
                tempPieceNum = true;
                break;
            }
        }

        assert (tempPieceNum);
    }

}
