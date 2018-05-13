package com.erikmafo.chess2.search;

import com.erikmafo.chess.search.transposition.*;
import org.jetbrains.annotations.Nullable;

/**
 * Created by erikmafo on 19.11.17.
 */
public class TranspositionTable {

    private final Transposition[] transpositions;
    private final HashFunction hashFunction;

    public TranspositionTable() {
        this(1048583);
    }

    public TranspositionTable(int size) {
        this(new HashFunction(size));
    }

    public TranspositionTable(HashFunction hashFunction) {
        this.hashFunction = hashFunction;
        this.transpositions = new Transposition[hashFunction.getSize()];
    }

    public void store(Transposition transposition) {
        transpositions[hashFunction.applyAsInt(transposition.getKey())] = transposition;
    }

    @Nullable
    public Transposition retrieve(long key) {

        Transposition transposition = transpositions[hashFunction.applyAsInt(key)];

        if (transposition != null && transposition.getKey() != key) {
            return null;
        }

        return transposition;
    }
}
