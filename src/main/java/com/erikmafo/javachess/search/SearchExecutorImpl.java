package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;

import java.util.concurrent.*;

/**
 * Created by erikmafo on 26.11.16.
 */
public class SearchExecutorImpl implements SearchExecutor {

    private final MoveSearch moveSearch;
    private final ExecutorService executorService;

    public SearchExecutorImpl(MoveSearch moveSearch, ExecutorService executorService) {
        this.moveSearch = moveSearch;
        this.executorService = executorService;
    }

    @Override
    public Future<SearchResult> submitSearch(Board board, BoardToIntFunction boardToIntFunction, int depth) {

        return executorService.submit(() -> moveSearch.execute(board, boardToIntFunction, depth));

    }

    @Override
    public Future<SearchResult> submitSearch(Board board, BoardToIntFunction evaluation, long timeout, TimeUnit timeUnit) {

        Future<SearchResult> result = executorService.submit(() -> {

            int depth = 1000;

            return moveSearch.execute(board, evaluation, depth);

        });


        Executors.newSingleThreadScheduledExecutor().schedule(() ->
                executorService.shutdownNow(), timeout, timeUnit);


        return result;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return executorService.awaitTermination(timeout, timeUnit);
    }


    @Override
    public void shutdownNow() {
        executorService.shutdownNow();
    }





}
