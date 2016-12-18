package com.erikmafo.chess.search.transposition;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 12.12.16.
 */
public class TranspositionTableTest {

    private HashFunction hashFunction = mock(HashFunction.class);
    private int size = 10;
    private TranspositionTable transpositionTable;


    @Before
    public void setUp() throws Exception {
        when(hashFunction.getSize()).thenReturn(size);
        transpositionTable = new TranspositionTable(hashFunction);
    }

    @Test
    public void shouldNotGetArrayIndexOutOfBoundsException() throws Exception {
        transpositionTable.retrieve(Long.MAX_VALUE);
    }

    @Test
    public void shouldNotGetArrayIndexOutOfBoundsExceptionWithNegativeKey() throws Exception {
        transpositionTable.retrieve(Long.MIN_VALUE);
    }

    @Test
    public void shouldCheckForHashFunctionCollision() throws Exception {

        Transposition transposition = mock(Transposition.class);

        long key = 5L;
        long otherKey = 4L;

        when(transposition.getKey()).thenReturn(key);

        when(hashFunction.applyAsInt(key)).thenReturn(0);

        when(hashFunction.applyAsInt(otherKey)).thenReturn(0);

        assertThat(transpositionTable.retrieve(otherKey), is(nullValue()));
    }
}