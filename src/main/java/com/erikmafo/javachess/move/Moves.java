package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.movegenerator.MoveGenerationStrategy;

import java.util.List;

/**
 * Created by erikmafo on 27.11.16.
 */
public class Moves {


    public static Move valueOf(Board board, Square from, Square to) {

        List<Move> moves = board.getMoves(MoveGenerationStrategy.ALL_PSEUDO_LEGAL_MOVES);

        Move actualMove = null;

        for (Move move : moves) {
            if (from.equals(move.getFrom()) && to.equals(move.getTo())) {
                actualMove = move;
            }
        }

        return actualMove;
    }

}
