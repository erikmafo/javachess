package com.erikmafo.javachess.boardrep;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by erikmafo on 13.11.16.
 */
public class BoardCoordinateTest {



    @Test
    public void isBetween() throws Exception {


        BoardCoordinate sq = BoardCoordinate.A1;

        boolean result = sq.isBetween(BoardCoordinate.A2, BoardCoordinate.A3);


        assertThat(result, is(false));

    }


}