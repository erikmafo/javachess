package com.erikmafo.javachess.search;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by erikmafo on 12.12.16.
 */
public class TranspositionTableTest {


    @Test
    public void shouldNotGetArrayIndexOutOfBoundsException() throws Exception {

        TranspositionTable transpositionTable = new TranspositionTable(10);

        transpositionTable.retrieve(Long.MAX_VALUE);
    }

    @Test
    public void shouldNotGetArrayIndexOutOfBoundsExceptionWithNegativeKey() throws Exception {

        TranspositionTable transpositionTable = new TranspositionTable(10);

        transpositionTable.retrieve(Long.MIN_VALUE);
    }


}