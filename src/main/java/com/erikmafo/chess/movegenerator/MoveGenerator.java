package com.erikmafo.chess.movegenerator;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.move.Move;

import java.util.List;

/**
 * Created by erikmafo on 11.12.16.
 */
public interface MoveGenerator {



    List<Move> generateMoves(Board board);



}
