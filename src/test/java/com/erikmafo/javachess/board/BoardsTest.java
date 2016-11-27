package com.erikmafo.javachess.board;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by erikmafo on 27.11.16.
 */
public class BoardsTest {


    @Test
    public void newBoardShouldNotCauseError() throws Exception {

        Boards.newBoard();

    }
}