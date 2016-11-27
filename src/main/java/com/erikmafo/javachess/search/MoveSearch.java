package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;

/**
 * Created by erikmafo on 27.11.16.
 */
public interface MoveSearch {



    SearchResult execute(Board board, BoardToIntFunction boardToIntFunction, int depth);
}
