package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.BoardCoordinate;
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
    private final BoardCoordinate from = BoardCoordinate.E7;
    private final BoardCoordinate to = BoardCoordinate.F6;

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

        protected AbstractMoveImpl(MoveReceiver moveReceiver, BoardCoordinate from, BoardCoordinate to) {
            super(moveReceiver, from, to);
        }

        @Override
        protected void beforePlayComplete() {

        }

        @Override
        protected void beforeUndoComplete() {

        }
    }

}




