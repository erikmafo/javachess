package com.erikmafo.chess2.search;

import com.erikmafo.chess2.movegeneration.Move;

/**
 * Created by erikmafo on 19.11.17.
 */
public class Transposition {

    public enum Flag {
        EXACT,
        UPPER_BOUND,
        LOWER_BOUND
    }

    private final Flag flag;
    private final long key;
    private final int score;
    private final int depth;
    private final Move bestMove;


    public Transposition(Flag flag, long key, int score, int depth, Move bestMove) {
        this.flag = flag;
        this.key = key;
        this.score = score;
        this.depth = depth;
        this.bestMove = bestMove;
    }

    public Flag getFlag() {
        return flag;
    }

    public long getKey() {
        return key;
    }

    public int getScore() {
        return score;
    }

    public int getDepth() {
        return depth;
    }

    public Move getBestMove() {
        return bestMove;
    }
}
