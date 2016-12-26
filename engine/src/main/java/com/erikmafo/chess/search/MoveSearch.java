package com.erikmafo.chess.search;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.search.evaluation.BoardToIntFunction;

import java.util.List;

/**
 * Created by erikmafo on 27.11.16.
 */
public interface MoveSearch {



    SearchResult execute(Board board, BoardToIntFunction boardToIntFunction, int depth);

    SearchResult execute(Board board, BoardToIntFunction boardToIntFunction, int depth, List<Move> principleVariation);

}
