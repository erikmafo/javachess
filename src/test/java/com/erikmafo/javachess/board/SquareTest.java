package com.erikmafo.javachess.board;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by erikmafo on 13.11.16.
 */
public class SquareTest {



    @Test
    public void isBetween() throws Exception {


        Square sq = Square.A1;

        boolean result = sq.isBetween(Square.A2, Square.A3);


        assertThat(result, is(false));

    }


}