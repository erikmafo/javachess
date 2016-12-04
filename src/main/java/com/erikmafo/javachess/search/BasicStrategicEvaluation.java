package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.pieces.PieceColor;

/**
 * Created by erikmafo on 04.12.16.
 */
public class BasicStrategicEvaluation implements BoardToIntFunction {

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
            score += 8;
        } else {
            if (board.hasKingSideCastlingRight(color)) {
                score += 3;
            }
            if (board.hasQueenSideCastlingRight(color)) {
                score += 2;
            }
        }

        return score;
    }
}
