package com.kasparov;

/**
 * Used for checking if a square is being attacked.
 *
 * @author Eric Liu
 */
public class SquareAttacked {

    /**
     * Directions in which a knight can attack.
     */
    static int[] knightDirection = {-8, -19, -21, -12, 8, 19, 21, 12};

    /**
     * Directions in which a rook can attack.
     */
    static int[] rookDirection = {-1, -10, 1, 10};

    /**
     * Directions in which a bishop can attack.
     */
    static int[] bishopDirection = {-9, -11, 11, 9};

    /**
     * Directions in which a king can attack.
     */
    static int[] kingDirection = {-1, -10, 1, 10, -9, -11, 11, 9};

    /**
     * Checks if a square is being attacked.
     */
    public static boolean squareAttacked(int sqr,
                                         int side,
                                         BoardStructure boardStructure) {
        int piece;
        int tempSqr;
        int dir;

        // Pawns:
        if (side == BoardColor.WHITE.value) {
            if (boardStructure.pieces[sqr - 11] == BoardPiece.WHITE_PAWN.value ||
                boardStructure.pieces[sqr - 9] == BoardPiece.WHITE_PAWN.value)
                return true;
        } else {
            if (boardStructure.pieces[sqr + 11] == BoardPiece.BLACK_PAWN.value ||
                    boardStructure.pieces[sqr + 9] == BoardPiece.BLACK_PAWN.value)
                return true;
        }

        // Knights:
        for (int i = 0; i < 8; i++) {
            piece = boardStructure.pieces[sqr + knightDirection[i]];
            if (BoardConstants.isKnight(piece) && BoardConstants.pieceColor[piece] == side)
                return true;
        }

        // Rook or Queen:
        for (int i = 0; i < 4; i++) {
            dir = rookDirection[i];
            tempSqr = sqr + dir;
            piece = boardStructure.pieces[tempSqr];
            while (piece != BoardSquare.OFFBOARD.value) {
                if (piece != BoardPiece.EMPTY.value) {
                    if (BoardConstants.isRookOrQueen(piece) && BoardConstants.pieceColor[piece] == side)
                        return true;
                    break;
                }
                tempSqr += dir;
                piece = boardStructure.pieces[tempSqr];
            }
        }

        // Bishop or Queen:
        for (int i = 0; i < 4; i++) {
            dir = bishopDirection[i];
            tempSqr = sqr + dir;
            piece = boardStructure.pieces[tempSqr];
            while (piece != BoardSquare.OFFBOARD.value) {
                if (piece != BoardPiece.EMPTY.value) {
                    if (BoardConstants.isBishopOrQueen(piece) && BoardConstants.pieceColor[piece] == side)
                        return true;
                    break;
                }
                tempSqr += dir;
                piece = boardStructure.pieces[tempSqr];
            }
        }

        // Kings:
        for (int i = 0; i < 8; i++) {
            piece = boardStructure.pieces[sqr + kingDirection[i]];
            if (BoardConstants.isKing(piece) && BoardConstants.pieceColor[piece] == side)
                return true;
        }

        return false;
    }

}
