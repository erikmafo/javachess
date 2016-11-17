package com.erikmafo.javachess.moves;

import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.MoveTarget;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 16.11.16.
 */
public class QuietMoveTest {


    private final MoveTarget moveTarget = mock(MoveTarget.class);

    private final BoardCoordinate from = BoardCoordinate.A2;
    private final BoardCoordinate to = BoardCoordinate.A5;

    private final Move move = new QuietMove(from, to);


    @Test
    public void executeQuietMoveCorrectly() throws Exception {

        move.execute(moveTarget);

        verify(moveTarget).movePiece(from, to);
    }


    @Test
    public void rewindMoveCorrectly() throws Exception {

        move.rewind(moveTarget);

        verify(moveTarget).movePieceBackwards(to, from);

    }
}