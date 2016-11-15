package com.erikmafo.javachess.search;

import com.erikmafo.javachess.boardrep.ReadableBoard;
import com.erikmafo.javachess.pieces.PieceColor;

/**
 * Created by erikmafo on 15.11.16.
 */
public interface EvaluationFunction {
    void setColor(PieceColor color);

    int evaluate(ReadableBoard board);
}
