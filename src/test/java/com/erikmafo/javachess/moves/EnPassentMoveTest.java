package com.erikmafo.javachess.moves;


import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.MoveTarget;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 13.11.16.
 */
public class EnPassentMoveTest {

    private BoardCoordinate from = BoardCoordinate.D6;
    private BoardCoordinate to = BoardCoordinate.C5;

    private EnPassentMove enPassentMove = new EnPassentMove(from, to);

    private MoveTarget moveTarget = mock(MoveTarget.class);

    @Test
    public void executeEnPassentMoveCorrectly() throws Exception {

        enPassentMove.execute(moveTarget);

        verify(moveTarget).movePieceAndDoCapture(from, to, BoardCoordinate.C6);

    }


    @Test
    public void rewindEnPassentMoveCorrectly() throws Exception {

        enPassentMove.rewind(moveTarget);

        verify(moveTarget).movePieceBackwards(to, from);
        verify(moveTarget).undoLastCapture();

    }



}