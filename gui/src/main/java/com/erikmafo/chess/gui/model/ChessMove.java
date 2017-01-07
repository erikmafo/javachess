package com.erikmafo.chess.gui.model;

import com.erikmafo.chess.board.Square;

/**
 * Created by erikmafo on 07.01.17.
 */
public class ChessMove {

    private final Square from;
    private final Square to;

    public ChessMove(Square from, Square to) {
        this.from = from;
        this.to = to;
    }

    public Square getFrom() {
        return from;
    }

    public Square getTo() {
        return to;
    }

    public String toLongAlgebraic() {
        return ("" + from + to).toLowerCase();
    }

    @Override
    public String toString() {
        return toLongAlgebraic();
    }
}
