package com.erikmafo.chess.board;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by erikmafo on 13.11.16.
 */
@RunWith(JUnitParamsRunner.class)
public class SquareTest {


    private Object[] hasNext_testFixtures() {
        return new Object[]{
                new Object[]{Square.A1, BasicOffset.LEFT, false},
                new Object[]{Square.A1, BasicOffset.DOWN_LEFT, false},
                new Object[]{Square.A1, BasicOffset.DOWN, false},
                new Object[]{Square.A1, BasicOffset.RIGHT, true},
                new Object[]{Square.A1, BasicOffset.UP_RIGHT, true},
                new Object[]{Square.A1, BasicOffset.UP, true}
        };
    }


    @Test
    @Parameters(method = "hasNext_testFixtures")
    public void testHasNext(Square square, Offset offset, boolean expected) throws Exception {

        assertThat(square.hasNext(offset), is(expected));

    }


    private Object[] getNext_testFixtures() {
        return new Object[]{
                new Object[]{Square.E4, BasicOffset.UP, Square.E5},
                new Object[]{Square.E4, BasicOffset.RIGHT, Square.F4},
                new Object[]{Square.E4, BasicOffset.DOWN_LEFT, Square.D3},
                new Object[]{Square.E4, BasicOffset.DOWN, Square.E3},
                new Object[]{Square.E4, BasicOffset.DOWN_RIGHT, Square.F3}
        };
    }


    @Test
    @Parameters(method = "getNext_testFixtures")
    public void testNext(Square square, Offset offset, Square expected) throws Exception {

        assertThat(square.next(offset), is(expected));

    }

    @Test
    public void isBetween() throws Exception {


        Square sq = Square.A1;

        boolean result = sq.isBetween(Square.A2, Square.A3);


        assertThat(result, is(false));

    }


}