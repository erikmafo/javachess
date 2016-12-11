package com.erikmafo.javachess.search;

import com.erikmafo.javachess.move.Move;

/**
 * Created by erikf on 12/11/2016.
 */
public class Node {

    public enum Flag {
        EXACT,
        BETA,
        ALPHA
    }

    private final Flag flag;
    private final long key;
    private final int score;
    private final int depth;
    private final Move bestMove;


    public Node(Flag flag, long key, int score, int depth, Move bestMove) {
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
