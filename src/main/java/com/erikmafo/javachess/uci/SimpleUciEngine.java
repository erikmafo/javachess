package com.erikmafo.javachess.uci;


import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.search.SearchExecutor;

import java.util.concurrent.CompletableFuture;

/**
 * Created by erikf on 14.07.2016.
 */
public class SimpleUciEngine implements UciEngine {


    private SearchExecutor searchExecutor;


    private final UciEngineDescription engineDescription;
    private final FenParser fenParser;

    private boolean debug = false;
    private Board board;


    public SimpleUciEngine(UciEngineDescription engineDescription, FenParser fenParser) {
        this.engineDescription = engineDescription;
        this.fenParser = fenParser;
    }

    @Override
    public UciEngineDescription uci() {
        return engineDescription;
    }

    @Override
    public void debug(boolean d) {
        debug = d;
    }

    @Override
    public boolean isReady() {
        //CompletableFuture<Boolean> res = new CompletableFuture<>();
        //res.complete(true);
        return true;
    }

    @Override
    public void setOption(String name, String value) {

    }

    @Override
    public void uciNewGame() {

    }

    @Override
    public void position(String fenOrStartPosition, String... moves) throws FenParseException {

        if ("startpos".equals(fenOrStartPosition)) {
            board = fenParser.parse(FenParser.START_POSITION);
        } else {
            board = fenParser.parse(fenOrStartPosition, moves);
        }

    }

    @Override
    public CompletableFuture<UciSearchResult> go(Search search, SearchProgressListener listener) {



        CompletableFuture<UciSearchResult> res = new CompletableFuture<>();

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                res.complete(new UciSearchResult("e2e4", "d2d4"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return res;
    }

    @Override
    public void stop() {
        searchExecutor.shutdownNow();
    }

    @Override
    public void ponderHit() {

    }

    @Override
    public void quit() {

    }
}
