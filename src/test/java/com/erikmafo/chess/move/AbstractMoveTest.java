package com.erikmafo.chess.move;

import com.erikmafo.chess.board.MoveReceiver;
import com.erikmafo.chess.board.Square;
import org.junit.Test;
import org.mockito.InOrder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by erikmafo on 20.11.16.
 */
public class AbstractMoveTest {


    private final MoveReceiver moveReceiver = mock(MoveReceiver.class);
    private final Square from = Square.E7;
    private final Square to = Square.F6;

    private final AbstractMove move = spy(new AbstractMoveImpl(moveReceiver, from, to));

    private final InOrder inOrder = inOrder(moveReceiver, move);

    @Test
    public void constructorShouldSetFrom() throws Exception {

        assertThat(move.getFrom(), is(from));

    }


    @Test
    public void constructorShouldSetTo() throws Exception {

        assertThat(move.getTo(), is(to));

    }

    @Test
    public void play() throws Exception {

        move.play();

        inOrder.verify(moveReceiver).movePiece(from, to);
        inOrder.verify(move).beforePlayComplete();
        inOrder.verify(moveReceiver).completePlay();
    }

    @Test
    public void undo() throws Exception {

        move.undo();

        inOrder.verify(moveReceiver).movePiece(to, from);
        inOrder.verify(move).beforeUndoComplete();
        inOrder.verify(moveReceiver).completeUndo();

    }

    class AbstractMoveImpl extends AbstractMove {

        protected AbstractMoveImpl(MoveReceiver moveReceiver, Square from, Square to) {
            super(moveReceiver, from, to, 0);
        }

        @Override
        protected void beforePlayComplete() {

        }

        @Override
        protected void beforeUndoComplete() {

        }
    }

}




