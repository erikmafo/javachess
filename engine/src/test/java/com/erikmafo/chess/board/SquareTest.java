package com.erikmafo.chess.board;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.erikmafo.chess.board.Square.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by erikmafo on 13.11.16.
 */
@RunWith(JUnitParamsRunner.class)
public class SquareTest {

    private static Object[] getRank_testData() {
        return new Object[]{
                new Object[]{0, A1, B1, C1, D1, E1, F1, G1, H1},
                new Object[]{1, A2, B2, C2, D2, E2, F2, G2, H2},
                new Object[]{2, A3, B3, C3, D3, E3, F3, G3, H3},
                new Object[]{3, A4, B4, C4, D4, E4, F4, G4, H4},
                new Object[]{4, A5, B5, C5, D5, E5, F5, G5, H5},
                new Object[]{5, A6, B6, C6, D6, E6, F6, G6, H6},
                new Object[]{6, A7, B7, C7, D7, E7, F7, G7, H7},
                new Object[]{7, A8, B8, C8, D8, E8, F8, G8, H8}
        };
    }

    @Test
    @Parameters(method = "getRank_testData")
    public void shouldReturnCorrectRank(int rank, Square[] squares) {

        for (Square square : squares) {
            assertThat("is correct rank", square.getRank(), is(rank));
        }
    }


    private static Object[] getFile_testData() {
        return new Object[]{
                new Object[]{0, A1, A2, A3, A4, A5, A6, A7, A8},
                new Object[]{1, B1, B2, B3, B4, B5, B6, B7, B8},
                new Object[]{2, C1, C2, C3, C4, C5, C6, C7, C8},
                new Object[]{3, D1, D2, D3, D4, D5, D6, D7, D8},
                new Object[]{4, E1, E2, E3, E4, E5, E6, E7, E8},
                new Object[]{5, F1, F2, F3, F4, F5, F6, F7, F8},
                new Object[]{6, G1, G2, G3, G4, G5, G6, G7, G8},
                new Object[]{7, H1, H2, H3, H4, H5, H6, H7, H8},
        };
    }

    @Test
    @Parameters(method = "getFile_testData")
    public void shouldReturnCorrectFile(int file, Square[] squares) {

        for (Square square : squares) {
            assertThat("is correct file", square.getFile(), is(file));
        }
    }

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
    public void testHasNext(Square square, Offset offset, boolean expected) {

        assertThat(square.hasNext(offset), is(expected));

    }


    private Object[] next_testFixtures() {
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
    @Parameters(method = "next_testFixtures")
    public void testNext(Square square, Offset offset, Square expected) {

        assertThat(square.next(offset), is(expected));

    }


    @Test
    public void shouldReturnOffBoardIfSlideToFar() {
        Square square = Square.A1;
        Square dest = square.next(BasicOffset.LEFT, 7);
        assertThat(dest, is(Square.OFF_BOARD));
    }


    @Test
    public void isBetween() {


        Square sq = Square.A1;

        boolean result = sq.isBetween(Square.A2, Square.A3);


        assertThat(result, is(false));

    }

    private Object[] getIndex64Fixtures() {
        return new Object[]{
                new Object[]{Square.A1, 0},
                new Object[]{Square.H1, 7},
                new Object[]{Square.A8, 56},
                new Object[]{Square.H8, 63}
        };
    }


    @Test
    @Parameters(method = "getIndex64Fixtures")
    public void shouldSetCorrectIndex64(Square square, int expectedIndex64) {
        assertThat(square.getIndex64(), is(expectedIndex64));
    }


}