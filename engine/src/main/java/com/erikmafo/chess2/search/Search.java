package com.erikmafo.chess2.search;

import com.erikmafo.chess2.movegeneration.Board;


public class Search {

    private final Board startingPosition;
    private final SearchAlgorithm searchAlgorithm;
    private final int depth;

    public Search(Board board, SearchAlgorithm searchAlgorithm, int depth) {
        this.startingPosition = board;
        this.searchAlgorithm = searchAlgorithm;
        this.depth = depth;
    }

    public SearchResult execute() {
        return searchAlgorithm.execute(startingPosition, depth);
    }
}
