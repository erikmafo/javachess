package com.erikmafo.chess2.search;

import com.erikmafo.chess2.movegeneration.Move;
import com.erikmafo.chess2.movegeneration.Board;

import java.util.List;

/**
 * Created by erikmafo on 20.11.17.
 */
public interface SearchAlgorithm {

    SearchResult execute(Board board, int depth);
}
