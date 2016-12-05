package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.pieces.PieceColor;

/**
 * Created by erikmafo on 04.12.16.
 */
public final class CastlingEvaluation implements BoardToIntFunction {

    public static final int DEFAULT_CASTLING_ADVANTAGE = 8;
    public static final int DEFAULT_KING_SIDE_CASTLING_RIGHT_ADVANTAGE = 3;
    public static final int DEFAULT_QUEEN_SIDE_CASTLING_RIGHT_ADVANTAGE = 2;

    private final int castlingAdvantage;
    private final int kingSideCastlingRightAdvantage;
    private final int queenSideCastlingRightAdvantage;

    public CastlingEvaluation(int castlingAdvantage, int kingSideCastlingRightAdvantage, int queenSideCastlingRightAdvantage) {
        this.castlingAdvantage = castlingAdvantage;
        this.kingSideCastlingRightAdvantage = kingSideCastlingRightAdvantage;
        this.queenSideCastlingRightAdvantage = queenSideCastlingRightAdvantage;
    }

    public CastlingEvaluation() {
        this(DEFAULT_CASTLING_ADVANTAGE,
                DEFAULT_KING_SIDE_CASTLING_RIGHT_ADVANTAGE,
                DEFAULT_QUEEN_SIDE_CASTLING_RIGHT_ADVANTAGE);
    }



    @Override
    public int applyAsInt(Board board) {

        PieceColor currentColor = board.getColorToMove();

        int currentColorScore = getScore(board, currentColor);

        int opponentScore = getScore(board, currentColor.getOpposite());

        return currentColorScore - opponentScore;
    }

    private int getScore(Board board, PieceColor color) {
        int score = 0;

        if (board.hasCastled(color)) {
            score += castlingAdvantage;
        } else {
            if (board.hasKingSideCastlingRight(color)) {
                score += kingSideCastlingRightAdvantage;
            }
            if (board.hasQueenSideCastlingRight(color)) {
                score += queenSideCastlingRightAdvantage;
            }
        }

        return score;
    }
}
