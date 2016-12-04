package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by erikmafo on 26.11.16.
 */
public class MaterialBoardEvaluation implements BoardToIntFunction {




    private final Map<PieceType, Integer> pieceValues = new HashMap<>();
    




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
                throw new AssertionError();
        }
        return material;
    }


}
