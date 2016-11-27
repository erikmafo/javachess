package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.BoardCoordinate;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 18.11.16.
 */
public class QuietMoveTest {


    private MoveReceiver moveReceiver = mock(MoveReceiver.class);
    private BoardCoordinate from = BoardCoordinate.A2;
    private BoardCoordinate to = BoardCoordinate.A5;

    private Move move = new QuietMove(moveReceiver, from, to);

    private InOrder inOrder = inOrder(moveReceiver);

    @Test
    public void playQuietMove() throws Exception {

        move.play();

        inOrder.verify(moveReceiver).movePiece(from, to);
        inOrder.verify(moveReceiver).completePlay();

    }


    @Test
    public void undoQuietMove() throws Exception {

        move.undo();

        inOrder.verify(moveReceiver).movePiece(to, from);
        inOrder.verify(moveReceiver).completeUndo();
    }
}