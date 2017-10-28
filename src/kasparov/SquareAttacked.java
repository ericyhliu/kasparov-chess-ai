package kasparov;

/**
 * Used for checking if a square is being attacked, and printing
 * out the board showing every square being attacked.
 *
 * @author Eric Liu
 */
public class SquareAttacked {

    /**
     * Checks if a square is being attacked.
     *
     * @param boardStructure
     * @param square
     * @param side
     * @return true if the square is being attacked, false otherwise
     * @throws NullPointerException if the BoardStructure is null
     * @throws IllegalArgumentException if the square is invalid
     * @throws IllegalArgumentException if the side is invalid
     */
    protected static boolean isSquareAttacked(BoardStructure boardStructure,
                                              int square, int side) {
        if (boardStructure == null)
            throw new NullPointerException("null BoardStructure");
        if (square < BoardSquare.A1.value || square > BoardSquare.OFFBOARD.value)
            throw new IllegalArgumentException("invalid square");
        if (side < BoardColor.WHITE.value || side > BoardColor.BOTH.value)
            throw new IllegalArgumentException("invalid side");

        int piece, tempSquare, direction;

        if (side == BoardColor.WHITE.value) {
            if (boardStructure.getPiece(square - 11) == BoardPiece.WHITE_PAWN.value ||
                boardStructure.getPiece(square - 9) == BoardPiece.WHITE_PAWN.value)
                return true;
        } else {
            if (boardStructure.getPiece(square + 11) == BoardPiece.BLACK_PAWN.value ||
                boardStructure.getPiece(square + 9) == BoardPiece.BLACK_PAWN.value)
                return true;
        }

        for (int i = 0; i < 8; i++) {
            piece = boardStructure.getPiece(square + BoardUtils.getKnightDirection(i));

            if (piece == BoardSquare.OFFBOARD.value)
                continue;

            if (BoardUtils.isPieceKnight(piece) && BoardUtils.getPieceColor(piece) == side)
                return true;
        }

        for (int i = 0; i < 4; i++) {
            direction = BoardUtils.getRookDirection(i);
            tempSquare = square + direction;
            piece = boardStructure.getPiece(tempSquare);
            while (piece != BoardSquare.OFFBOARD.value) {
                if (piece != BoardPiece.EMPTY.value) {
                    if (BoardUtils.isPieceRookOrQueen(piece) &&
                        BoardUtils.getPieceColor(piece) == side)
                        return true;
                    break;
                }
                tempSquare += direction;
                piece = boardStructure.getPiece(tempSquare);
            }
        }

        for (int i = 0; i < 4; i++) {
            direction = BoardUtils.getBishopDirection(i);
            tempSquare = square + direction;
            piece = boardStructure.getPiece(tempSquare);
            while (piece != BoardSquare.OFFBOARD.value) {
                if (piece != BoardPiece.EMPTY.value) {
                    if (BoardUtils.isPieceBishopOrQueen(piece) &&
                        BoardUtils.getPieceColor(piece) == side)
                        return true;
                    break;
                }
                tempSquare += direction;
                piece = boardStructure.getPiece(tempSquare);
            }
        }

        for (int i = 0; i < 8; i++) {
            piece = boardStructure.getPiece(square + BoardUtils.getKingDirection(i));

            if (piece == BoardSquare.OFFBOARD.value)
                continue;

            if (BoardUtils.isPieceKing(piece) && BoardUtils.getPieceColor(piece) == side)
                return true;
        }

        return false;
    }

    /**
     * Print the board with X's denoting squares being attacked and
     * -'s denoting otherwise.
     *
     * @param boardStructure
     * @param side
     * @throws NullPointerException if the BoardStructure is null
     * @throws IllegalArgumentException if the side is invalid
     */
    protected static void showSquareAttacked(BoardStructure boardStructure,
                                             int side) {
        if (boardStructure == null)
            throw new NullPointerException("null BoardStructure");
        if (side < BoardColor.WHITE.value || side > BoardColor.BOTH.value)
            throw new IllegalArgumentException("invalid side");

        int square;
        for (int r = BoardRank.RANK_8.value; r >= BoardRank.RANK_1.value; r--) {
            for (int f = BoardFile.FILE_A.value; f <= BoardFile.FILE_H.value; f++) {
                square = BoardUtils.convertFileRankToSqr(f, r);
                if (isSquareAttacked(boardStructure, square, side))
                    System.out.print("X ");
                else
                    System.out.print("- ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }

}
