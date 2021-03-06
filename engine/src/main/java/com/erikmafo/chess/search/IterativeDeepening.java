package com.erikmafo.chess.search;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.search.evaluation.BoardToIntFunction;

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
    private final BoardToIntFunction evaluation;
    private final Clock clock;


    public IterativeDeepening(Board board, MoveSearch moveSearch, BoardToIntFunction evaluation, Clock clock) {
        this.board = board;
        this.moveSearch = moveSearch;
        this.evaluation = evaluation;
        this.clock = clock;
    }


    public SearchResult execute(int maxDepth, long duration, TimeUnit timeUnit) {

        Instant start = clock.instant();

        SearchResult searchResult = moveSearch.execute(board, evaluation, 1);

        for (int i = 2; i <= maxDepth; i++) {

            if (Duration.between(start, clock.instant()).toMillis() > timeUnit.toMillis(duration)) {
                break;
            }

            List<Move> principleVariation = searchResult.getPrincipleVariation();

            searchResult = moveSearch.execute(board, evaluation, i, principleVariation);
        }

        return searchResult;
    }


}
