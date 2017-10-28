package kasparov;

/**
 * General utility functions for moves.
 *
 * @author Eric Liu
 */
public class MoveUtils {

    /**
     * Flags for en passant, pawn start, castle, capture and promotion
     * bits set in a move int.
     */
    protected static final int MOVE_FLAG_EN_PASSANT = 0x40000;
    protected static final int MOVE_FLAG_PAWN_START = 0x80000;
    protected static final int MOVE_FLAG_CASTLE     = 0x1000000;
    protected static final int MOVE_FLAG_CAPTURE    = 0x7C000;
    protected static final int MOVE_FLAG_PROMOTED   = 0xF00000;

    /**
     * Gets the from square from a move int.
     *
     * @param move
     * @return the from square from a move int
     */
    protected static int from(int move) {
        return move & 0x7F;
    }

    /**
     * Gets the to square from a move int.
     * @param move
     * @return the to square from a move int
     */
    protected static int to(int move) {
        return (move >> 7) & 0x7F;
    }

    /**
     * Gets the captured bit from a move int.
     * @param move
     * @return the captured bit from a move int
     */
    protected static int captured(int move) {
        return (move >> 14) & 0xF;
    }

    /**
     * Gets the promotion bit from a move int.
     * @param move
     * @return the promotion bit from a move int
     */
    protected static int promoted(int move) {
        return (move >> 20) & 0xF;
    }

    /**
     * Parses the move string into int representation, with corresponding
     * en passant, pawn start, castle, capture and promotion bits set.
     *
     * @param boardStructure
     * @param moveStr
     * @return int representation of move
     * @throws NullPointerException if the BoardStructure is null
     * @throws IllegalArgumentException if the move string is empty
     */
    protected static int parseMove(BoardStructure boardStructure,
                                   String moveStr) {
        if (boardStructure == null)
            throw new NullPointerException("null BoardStructure");
        if (moveStr.length() == 0)
            throw new IllegalArgumentException("empty move string");

        if (moveStr.charAt(1) > '8' || moveStr.charAt(1) < '1' ||
            moveStr.charAt(3) > '8' || moveStr.charAt(3) < '1' ||
            moveStr.charAt(0) > 'h' || moveStr.charAt(0) < 'a' ||
            moveStr.charAt(2) > 'h' || moveStr.charAt(2) < 'a' )
            return BoardUtils.NO_MOVE;

        int from = BoardUtils.convertFileRankToSqr(moveStr.charAt(0) - 'a',
                moveStr.charAt(1) - '1');
        int to = BoardUtils.convertFileRankToSqr(moveStr.charAt(2) - 'a',
                moveStr.charAt(3) - '1');

        MoveList moveList = new MoveList();
        MoveGenerator.generateAllMoves(boardStructure, moveList);
        int move, promotedPiece;

        for (int i = 0; i < moveList.getCount(); i++) {
            move = moveList.getMove(i).getMove();
            if (from(move) == from && to(move) == to) {
                promotedPiece = promoted(move);
                if (promotedPiece != BoardPiece.EMPTY.value) {
                    if (BoardUtils.isPieceRookOrQueen(promotedPiece) &&
                        !BoardUtils.isPieceBishopOrQueen(promotedPiece) &&
                        moveStr.charAt(4) == 'r')
                        return move;
                    else if (!BoardUtils.isPieceRookOrQueen(promotedPiece) &&
                            BoardUtils.isPieceBishopOrQueen(promotedPiece) &&
                            moveStr.charAt(4) == 'b')
                        return move;
                    else if (BoardUtils.isPieceRookOrQueen(promotedPiece) &&
                            BoardUtils.isPieceBishopOrQueen(promotedPiece) &&
                            moveStr.charAt(4) == 'q')
                        return move;
                    else if (BoardUtils.isPieceKnight(promotedPiece) &&
                            moveStr.charAt(4) == 'n')
                        return move;
                    continue;
                }
                return move;
            }
        }
        return BoardUtils.NO_MOVE;
    }

}
