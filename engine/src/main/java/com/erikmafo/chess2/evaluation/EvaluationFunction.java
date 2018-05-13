package com.erikmafo.chess2.evaluation;

import com.erikmafo.chess2.movegeneration.Board;

@FunctionalInterface
public interface EvaluationFunction {

    /**
     * The function shall evaluate the board and return an integer score.
     *
     * <p>
     * A score greater than 0 is good for the active color and a score less
     * than zero is good for the active colors opponent. A score equal to
     * 0 means that the position is equal.
     * <p>
     *
     * @param board - the board to evaluate
     * @return - an integer
     */
    int evaluate(Board board);
}
