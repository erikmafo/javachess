package com.erikmafo.chess.movegenerator;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.move.Move;

import java.util.List;

/**
 * Created by erikmafo on 19.11.16.
 */
public interface MoveGeneratorDelegate {

    List<Move> generateMoves(Board board, Square from);


}
