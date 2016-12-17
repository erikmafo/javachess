package com.erikmafo.chess.uci;

import java.util.Collections;
import java.util.List;

/**
 * Created by erikf on 13.07.2016.
 */
public class Search {

    private final List<String> searchMoves;
    private final boolean ponder = false;
    private final long wTime;
    private final long bTime;


    public Search(List<String> searchMoves, long wTime, long bTime) {
        this.searchMoves = searchMoves;
        this.wTime = wTime;
        this.bTime = bTime;
    }

    public static class Builder {
        private List<String> searchMoves = Collections.EMPTY_LIST;
        private long wTime = 1000 * 60 * 60 * 5;
        private long bTime = 1000 * 60 * 60 * 5;

        public Builder searchMoves(List<String> searchMoves) {
            this.searchMoves = searchMoves;
            return this;
        }

        public Builder wTime(long wTime) {
            this.wTime = wTime;
            return this;
        }

        public Builder bTime(long bTime) {
            this.bTime = bTime;
            return this;
        }

        public Search build() {
            return new Search(searchMoves, wTime, bTime);
        }
    }



    public List<String> searchMoves() {
        return searchMoves;
    }

    public boolean ponder() {
        return ponder;
    }




}
