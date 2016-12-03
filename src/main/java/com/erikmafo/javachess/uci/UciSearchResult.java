package com.erikmafo.javachess.uci;

/**
 * Created by erikf on 14.07.2016.
 */
public class UciSearchResult {

    private final String bestMove;
    private final String ponder;

    public UciSearchResult(String bestMove, String ponder) {
        this.bestMove = bestMove;
        this.ponder = ponder;
    }

    public String bestMove() {
        return bestMove;
    }

    public String ponder() {
        return ponder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UciSearchResult)) return false;

        UciSearchResult that = (UciSearchResult) o;

        if (bestMove != null ? !bestMove.equals(that.bestMove) : that.bestMove != null) return false;
        return ponder != null ? ponder.equals(that.ponder) : that.ponder == null;

    }

    @Override
    public int hashCode() {
        int result = bestMove != null ? bestMove.hashCode() : 0;
        result = 31 * result + (ponder != null ? ponder.hashCode() : 0);
        return result;
    }
}
