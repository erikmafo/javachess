package com.erikmafo.javachess.search;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by erikf on 12/11/2016.
 */
public class TranspositionTable {


    private final int size;
    private final Transposition[] transpositions;

    public TranspositionTable() {
        this(1048583);
    }


    public TranspositionTable(int size) {
        this.size = size;
        this.transpositions = new Transposition[size];
    }


    public void store(Transposition transposition) {
        transpositions[map(transposition.getKey())] = transposition;
    }

    private long mod(long a, int b) {
        long c = a % b;
        return (c < 0) ? c + b : c;
    }

    private int map(long key) {
        return (int) (mod(key, size));
    }


    public Transposition retrieve(long key) {


        Transposition transposition = transpositions[map(key)];

        if (transposition != null && transposition.getKey() != key) {
            return null;
        }

        return transposition;
    }


}
