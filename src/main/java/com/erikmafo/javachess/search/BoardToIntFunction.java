package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;

import java.util.function.ToIntFunction;

/**
 * Created by erikmafo on 26.11.16.
 */
@FunctionalInterface
public interface BoardToIntFunction {

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
    int applyAsInt(Board board);

}
