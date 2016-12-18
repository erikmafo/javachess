package com.erikmafo.chess.move;

import com.erikmafo.chess.board.MoveReceiver;
import com.erikmafo.chess.board.Square;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 19.11.16.
 */
@RunWith(JUnitParamsRunner.class)
public class DoublePawnPushMoveTest {

    private MoveReceiver moveReceiver = mock(MoveReceiver.class);
    private InOrder inOrder = inOrder(moveReceiver);

    private Object[] doublePawnPushTestFixtures() {
        return new Object[] {
                new Object[] {Square.A2, Square.A4, Square.A3},
                new Object[] {Square.E2, Square.E4, Square.E3},
                new Object[] {Square.E7, Square.E5, Square.E6},
                new Object[] {Square.A7, Square.A5, Square.A6}
        };
    }


    @Test
    @Parameters(method = "doublePawnPushTestFixtures")
    public void playDoublePawnPushMove(Square from, Square to, Square enPassentTarget) throws Exception {

        Move move = new DoublePawnPushMove(moveReceiver, from, to);

        move.play();

        verify(moveReceiver).resetHalfMoveClock();
        verify(moveReceiver).setEnPassentTarget(enPassentTarget);
        inOrder.verify(moveReceiver).movePiece(from, to);
        inOrder.verify(moveReceiver).completePlay();
    }


    @Test
    @Parameters(method = "doublePawnPushTestFixtures")
    public void undoDoublePawnPushMove(Square from, Square to, Square enPassentTarget) throws Exception {
        Move move = new DoublePawnPushMove(moveReceiver, from, to);

        move.undo();

        verify(moveReceiver).removeEnPassentTarget();
        inOrder.verify(moveReceiver).movePiece(to, from);
        inOrder.verify(moveReceiver).completeUndo();
    }

}