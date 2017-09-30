package com.kasparov;

/**
 * For generating a position (hash) key.
 *
 * @author Eric Liu
 */
public class PositionKey {

    public static long generatePositionKey(BoardStructure boardStructure) {
        int sqr;
        long finalKey = 0;
        int piece;

        for (sqr = 0; sqr < BoardConstants.BOARD_SQR_NUM; sqr++) {
            piece = boardStructure.pieces[sqr];

            if (piece != BoardSquare.NONE.value && piece != BoardPiece.EMPTY.value) {
                System.out.println(piece >= BoardPiece.WHITE_PAWN.value &&
                                   piece <= BoardPiece.BLACK_KING.value);
                finalKey ^= boardStructure.pieceKeys[piece][sqr];
            }
        }

        if (boardStructure.side == BoardColors.WHITE.value) {
            finalKey ^= boardStructure.sideKey;
        }

        if (boardStructure.enPassant != BoardSquare.NONE.value) {
            System.out.println(boardStructure.enPassant >= 0 &&
                               boardStructure.enPassant < BoardConstants.BOARD_SQR_NUM);
            finalKey ^= boardStructure.pieceKeys[BoardPiece.EMPTY.value][boardStructure.enPassant];
        }

        System.out.println(boardStructure.castlePerm >= 0 && boardStructure.castlePerm <= 15);

        finalKey ^= boardStructure.castleKeys[boardStructure.castlePerm];

        return finalKey;
    }

}
