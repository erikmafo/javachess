package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.search.evaluation.BoardToIntFunction;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by erikf on 12/10/2016.
 */
public class IterativeDeepening {


    private final Board board;
    private final MoveSearch moveSearch;
    private final Clock clock;


    public IterativeDeepening(Board board, MoveSearch moveSearch, Clock clock) {
        this.board = board;
        this.moveSearch = moveSearch;
        this.clock = clock;
    }


    public SearchResult execute(BoardToIntFunction evaluation, long duration, TimeUnit timeUnit) {

        Instant start = clock.instant();

        SearchResult searchResult = moveSearch.execute(board, evaluation, 1);

        for (int i = 2; i < 1000; i++) {

            if (Duration.between(start, clock.instant()).toMillis() > timeUnit.toMillis(duration)) {
                break;
            }

            List<Move> principleVariation = searchResult.getPrincipleVariation();

            searchResult = moveSearch.execute(board, evaluation, i, principleVariation);
        }

        return searchResult;
    }


}
