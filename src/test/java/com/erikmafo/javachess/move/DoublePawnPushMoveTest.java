package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.Square;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 19.11.16.
 */
public class DoublePawnPushMoveTest {

    private MoveReceiver moveReceiver = mock(MoveReceiver.class);
    private Square from = Square.A2;
    private Square to = Square.A4;
    private Square enPassentTarget = Square.A3;

    private Move move = new DoublePawnPushMove(moveReceiver, from, to);

    private InOrder inOrder = inOrder(moveReceiver);

    @Test
    public void playDoublePawnPushMove() throws Exception {

        move.play();

        verify(moveReceiver).resetHalfMoveClock();
        verify(moveReceiver).setEnPassentTarget(enPassentTarget);
        inOrder.verify(moveReceiver).movePiece(from, to);
        inOrder.verify(moveReceiver).completePlay();

    }


    @Test
    public void undoDoublePawnPushMove() throws Exception {

        move.undo();

        verify(moveReceiver).removeEnPassentTarget();
        inOrder.verify(moveReceiver).movePiece(to, from);
        inOrder.verify(moveReceiver).completeUndo();
    }

}