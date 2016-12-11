package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.move.Move;

import java.util.List;

/**
 * Created by erikmafo on 11.12.16.
 */
public interface MoveGenerator {



    List<Move> generateMoves(Board board);



}
