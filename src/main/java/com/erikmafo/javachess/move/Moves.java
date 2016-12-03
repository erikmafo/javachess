package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.movegenerator.MoveGenerationStrategy;
import com.erikmafo.javachess.movegenerator.MoveGenerators;

import java.util.List;

/**
 * Created by erikmafo on 27.11.16.
 */
public class Moves {


    public static Move valueOf(Board board, BoardCoordinate from, BoardCoordinate to) {

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
