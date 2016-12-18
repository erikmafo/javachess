package com.erikmafo.chess.board;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.everyItem;
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
                new Object[]{Square.E4, BasicOffset.DOWN_RIGHT, Square.F3},
                new Object[]{Square.A1, BasicOffset.LEFT, Square.OFF_BOARD},
                new Object[]{Square.A1, KnightOffset.KNIGHT_LEAP_2DOWN_LEFT, Square.OFF_BOARD},
                new Object[]{Square.A1, KnightOffset.KNIGHT_LEAP_UP_2LEFT, Square.OFF_BOARD}
        };
    }


    @Test
    @Parameters(method = "getNext_testFixtures")
    public void testNext(Square square, Offset offset, Square expected) throws Exception {

        assertThat(square.next(offset), is(expected));

    }


    @Test
    public void shouldReturnOffBoardIfSlideToFar() {
        Square square = Square.A1;
        Square dest = square.next(BasicOffset.LEFT, 7);
        assertThat(dest, is(Square.OFF_BOARD));
    }


    @Test
    public void isBetween() throws Exception {


        Square sq = Square.A1;

        boolean result = sq.isBetween(Square.A2, Square.A3);


        assertThat(result, is(false));

    }



    private Object[] getIndex64Fixtures() {
        return new Object[] {
                new Object[] {Square.A1, 0},
                new Object[] {Square.H1, 7},
                new Object[] {Square.A8, 56},
                new Object[] {Square.H8, 63}
        };
    }


    @Test
    @Parameters(method = "getIndex64Fixtures")
    public void shouldSetCorrectIndex64(Square square, int expectedIndex64) {
        assertThat(square.getIndex64(), is(expectedIndex64));
    }






}