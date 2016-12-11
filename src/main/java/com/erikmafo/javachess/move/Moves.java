package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.movegenerator.MoveGenerationStrategy;
import com.erikmafo.javachess.movegenerator.MoveGeneratorFactory;

import java.util.List;

/**
 * Created by erikmafo on 27.11.16.
 */
public class Moves {


    public static Move valueOf(Board board, Square from, Square to) {

        List<Move> moves = new MoveGeneratorFactory()
                .newInstance(MoveGenerationStrategy.ALL_PSEUDO_LEGAL_MOVES, new MoveFactory())
                .generateMoves(board);

        Move actualMove = null;

        for (Move move : moves) {
            if (from.equals(move.getFrom()) && to.equals(move.getTo())) {
                actualMove = move;
            }
        }

        return actualMove;
    }

}
