package com.erikmafo.chess2.search;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by erikmafo on 20.11.17.
 */
public class SearchExecutorService {

    private final ExecutorService executorService;

    public SearchExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Submits a search that will be executed in the background
     *
     * @param search - the search to execute
     *
     * @return a future with the search result
     */
    public Future<SearchResult> submit(Search search) {
        return executorService.submit(search::execute);
    }

    public void submit(Search search, SearchCallback callback) {
        executorService.submit(() -> callback.onResult(search, search.execute()));
    }


    public void shutdown() {
        executorService.shutdown();
    }




}
