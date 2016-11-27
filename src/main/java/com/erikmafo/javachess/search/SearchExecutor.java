package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by erikmafo on 26.11.16.
 */
public interface SearchExecutor {

    Future<SearchResult> submitSearch(Board board, BoardToIntFunction evaluationFunction, int depth);

    Future<SearchResult> submitSearch(Board board, BoardToIntFunction evaluationFunction, long timeout, TimeUnit timeUnit);

    boolean awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException;

    void shutdownNow();



}
