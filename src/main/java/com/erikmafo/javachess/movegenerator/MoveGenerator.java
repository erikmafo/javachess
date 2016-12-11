package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;

import java.util.List;

/**
 * Created by erikmafo on 19.11.16.
 */
public interface MoveGenerator {

    List<Move> generateMoves(Board board, Square from);


}
