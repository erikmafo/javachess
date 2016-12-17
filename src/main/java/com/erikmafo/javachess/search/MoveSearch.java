package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.search.evaluation.BoardToIntFunction;

import java.util.List;

/**
 * Created by erikmafo on 27.11.16.
 */
public interface MoveSearch {



    SearchResult execute(Board board, BoardToIntFunction boardToIntFunction, int depth);

    SearchResult execute(Board board, BoardToIntFunction boardToIntFunction, int depth, List<Move> principleVariation);

}
