package com.erikmafo.javachess.search;

import com.erikmafo.javachess.boardrep.ReadableBoard;
import com.erikmafo.javachess.pieces.PieceColor;


public class SimpleMaterialEvaluationFunction implements EvaluationFunction {


    private PieceColor color;

    @Override
    public void setColor(PieceColor color) {
        this.color = color;
    }


    private final static SimpleMaterialEvaluationFunction INSTANCE = new SimpleMaterialEvaluationFunction();

    private SimpleMaterialEvaluationFunction() {
    }

    @Override
    public int evaluate(ReadableBoard board) {

        int sumMaterial = board.sumMaterial(color) - board.sumMaterial(color.getOpposite());
        int positionScore = (board.getAttackedSquaresCount(color) -
                board.getAttackedSquaresCount(color.getOpposite()));

        int hasCastledScore = board.hasCastled(color) ? 10 : 0;
        int hasCastledScoreOpponent = board.hasCastled(color.getOpposite()) ? 10 : 0;

        positionScore = positionScore + 2 * (hasCastledScore - hasCastledScoreOpponent);

        return 10 * sumMaterial + positionScore;
    }

    public static SimpleMaterialEvaluationFunction getInstance() {
        return INSTANCE;
    }
}
