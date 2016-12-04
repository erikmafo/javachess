package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceType;

/**
 * Created by erikmafo on 26.11.16.
 */
public class MaterialBoardEvaluation implements BoardToIntFunction {


    /**
     * The function shall evaluate the board and return an integer score.
     * <p>
     * A score greater than 0 is good for the active color and a score less
     * than zero is good for the active colors opponent. A score equal to
     * 0 means that the position is equal.
     * <p>
     * This function associates the following value to each piece:
     * <ul>
     * <li>Pawn: 10
     * <li>Bishop: 30
     * <li>Knight: 30
     * <li>Rook: 50
     * <li>Queen: 90
     * <li>King: 10000
     * </ul>
     *
     * @param board - the board to evaluate
     * @return - an integer
     */
    @Override
    public int applyAsInt(Board board) {
        int sign = board.getColorToMove().isWhite() ? 1 : -1;

        int score = 0;
        for (Square square : board.getOccupiedSquares()) {

            if (board.isOccupied(square)) {

                Piece piece = board.getNullablePiece(square);

                int mod = piece.getColor().isWhite() ? 1 : -1;

                score += mod * getMaterial(piece.getType());
            }

        }

        return score * sign;
    }

    private int getMaterial(PieceType pieceType) {
        int material;
        switch (pieceType) {
            case PAWN:
                material = 10;
                break;
            case BISHOP:
                material = 30;
                break;
            case KNIGHT:
                material = 30;
                break;
            case ROOK:
                material = 50;
                break;
            case QUEEN:
                material = 90;
                break;
            case KING:
                material = 10000;
                break;
            default:
                material = 0;
                break;
        }
        return material;
    }


}
