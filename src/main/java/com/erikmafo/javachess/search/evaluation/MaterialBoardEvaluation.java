package com.erikmafo.javachess.search.evaluation;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by erikmafo on 26.11.16.
 */
public final class MaterialBoardEvaluation implements BoardToIntFunction {

    public static final int DEFAULT_PAWN_VALUE = 100;
    public static final int DEFAULT_BISHOP_VALUE = 300;
    public static final int DEFAULT_KNIGHT_VALUE = 288;
    public static final int DEFAULT_ROOK_VALUE = 500;
    public static final int DEFAULT_QUEEN_VALUE = 900;
    public static final int DEFAULT_KING_VALUE = 100000;


    private final int pawnValue;
    private final int bishopValue;
    private final int knightValue;
    private final int rookValue;
    private final int queenValue;
    private final int kingValue;


    MaterialBoardEvaluation(Map<PieceType, Integer> pieceValues) {
        this.pawnValue = pieceValues.getOrDefault(PieceType.PAWN, DEFAULT_PAWN_VALUE);
        this.bishopValue = pieceValues.getOrDefault(PieceType.BISHOP, DEFAULT_BISHOP_VALUE);
        this.knightValue = pieceValues.getOrDefault(PieceType.KNIGHT, DEFAULT_KNIGHT_VALUE);
        this.rookValue = pieceValues.getOrDefault(PieceType.ROOK, DEFAULT_ROOK_VALUE);
        this.queenValue = pieceValues.getOrDefault(PieceType.QUEEN, DEFAULT_QUEEN_VALUE);
        this.kingValue = pieceValues.getOrDefault(PieceType.KING, DEFAULT_KING_VALUE);
    }

    public MaterialBoardEvaluation() {
        this(Collections.emptyMap());
    }



    public static class Builder {

        private final Map<PieceType, Integer> values = new HashMap<>();

        public Builder setValue(PieceType pieceType, int value) {
            values.put(pieceType, value);
            return this;
        }

        public MaterialBoardEvaluation build() {
            return new MaterialBoardEvaluation(values);
        }

    }


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
                material = pawnValue;
                break;
            case BISHOP:
                material = bishopValue;
                break;
            case KNIGHT:
                material = knightValue;
                break;
            case ROOK:
                material = rookValue;
                break;
            case QUEEN:
                material = queenValue;
                break;
            case KING:
                material = kingValue;
                break;
            default:
                throw new AssertionError();
        }
        return material;
    }


}
