package com.erikmafo.javachess.boardrep;

import com.erikmafo.javachess.moves.Move;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 13.11.16.
 */
public class BoardTest {

    private Board board = new Board();

    private Move move = mock(Move.class);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void playShouldExecuteMove() throws Exception {

        board.play(move);

        verify(move).execute(board);

    }

    @Test
    public void undoLastShouldRewindTheLastMoveThatWasPlayed() throws Exception {

        board.play(move);

        board.undoLast();

        verify(move).rewind(board);

    }


    @Test(expected = IllegalStateException.class)
    public void undoLastShouldThrowIllegalStateExceptionIfNoMovesBeenPlayed() throws Exception {
        board.undoLast();
    }






}