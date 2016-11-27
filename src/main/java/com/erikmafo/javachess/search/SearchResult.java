package com.erikmafo.javachess.search;

import com.erikmafo.javachess.move.Move;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by erikmafo on 26.11.16.
 */
public class SearchResult {


    private final int score;
    private final List<Move> principleVariation;

    public SearchResult(int score, Move... principleVariation) {
        this.score = score;
        this.principleVariation = Arrays.asList(principleVariation);
    }


    public Move getBestMove() {
        return principleVariation.get(0);
    }

    public int getScore() {
        return score;
    }

    public List<Move> getPrincipleVariation() {
        return new ArrayList<>(principleVariation);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchResult)) return false;

        SearchResult result = (SearchResult) o;

        if (score != result.score) return false;
        return principleVariation.equals(result.principleVariation);

    }

    @Override
    public int hashCode() {
        int result = score;
        result = 31 * result + principleVariation.hashCode();
        return result;
    }
}
