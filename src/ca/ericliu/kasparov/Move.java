package ca.ericliu.kasparov;

/**
 * Move structure.
 *
 * @author Eric Liu
 */
public class Move {
    int move;
    int score;

    static int from(int move) {
        return move & 0x7F;
    }

    static int to(int move) {
        return (move >> 7) & 0x7F;
    }

    static int captured(int move) {
        return (move >> 14) & 0xF;
    }

    static int promoted(int move) {
        return (move >> 20) & 0xF;
    }

    static int moveFlagEnPassant = 0x40000;

    static int moveFlagPawnStart = 0x80000;

    static int moveFlagCastle = 0x1000000;

    static int moveFlagCapture = 0x7C000;

    static int moveFlagPromoted = 0xF00000;

    static int parseMove(BoardStructure boardStructure, String s) {
        if (s.charAt(1) > '8' || s.charAt(1) < '1')
            return BoardConstants.NO_MOVE;
        if (s.charAt(3) > '8' || s.charAt(3) < '1')
            return BoardConstants.NO_MOVE;
        if (s.charAt(0) > 'h' || s.charAt(0) < 'a')
            return BoardConstants.NO_MOVE;
        if (s.charAt(2) > 'h' || s.charAt(2) < 'a')
            return BoardConstants.NO_MOVE;

        int from = BoardConstants.convertFileRankToSqr(s.charAt(0)-'a', s.charAt(1)-'1');
        int to = BoardConstants.convertFileRankToSqr(s.charAt(2)-'a', s.charAt(3)-'1');

        assert(Validate.isSquareOnBoard(boardStructure, from) &&
                Validate.isSquareOnBoard(boardStructure, to));

        MoveList moveList = new MoveList();
        MoveGenerator.generateAllMoves(boardStructure, moveList);
        int moveNum = 0;
        int move = 0;
        int promotedPiece = BoardPiece.EMPTY.value;

        for (moveNum = 0; moveNum < moveList.count; moveNum++) {
            move = moveList.moves[moveNum].move;
            if (from(move) == from && to(move) == to) {
                promotedPiece = promoted(move);
                if (promotedPiece != BoardPiece.EMPTY.value) {
                    if (BoardConstants.isRookOrQueen(promotedPiece) &&
                        !BoardConstants.isBishopOrQueen(promotedPiece) &&
                            s.charAt(4) == 'r')
                        return move;
                    else if (!BoardConstants.isRookOrQueen(promotedPiece) &&
                             BoardConstants.isBishopOrQueen(promotedPiece) &&
                            s.charAt(4) == 'b')
                        return move;
                    else if (BoardConstants.isRookOrQueen(promotedPiece) &&
                             BoardConstants.isBishopOrQueen(promotedPiece) &&
                             s.charAt(4) == 'q')
                        return move;
                    else if (BoardConstants.isKnight(promotedPiece) &&
                            s.charAt(4) == 'n')
                        return move;
                    continue;
                }
                return move;
            }
        }
        return BoardConstants.NO_MOVE;
    }


    public Move() {}

    public Move(int move, int score) {
        this.move = move;
        this.score = score;
    }

}
