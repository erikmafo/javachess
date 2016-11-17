package com.erikmafo.javachess.moves;

import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.MoveTarget;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 16.11.16.
 */
public class CaptureMoveTest {

    private final MoveTarget moveTarget = mock(MoveTarget.class);

    private final BoardCoordinate from = BoardCoordinate.A2;
    private final BoardCoordinate to = BoardCoordinate.A5;

    private final Move move = new CaptureMove(from, to);


    @Test
    public void executeCaptureMoveCorrectly() throws Exception {

        move.execute(moveTarget);

        verify(moveTarget).movePieceAndDoCapture(from, to, to);


    }


    @Test
    public void rewindCaptureCorrectly() throws Exception {

        move.rewind(moveTarget);

        verify(moveTarget).movePieceBackwards(to, from);

        verify(moveTarget).undoLastCapture();

    }


}