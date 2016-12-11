package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.pieces.Piece;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by erikmafo on 19.11.16.
 */
public interface MoveGeneratorDelegate {

    List<Move> generateMoves(Board board, Square from);


}
