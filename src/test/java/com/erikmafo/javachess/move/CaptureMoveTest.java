package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.pieces.Piece;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 18.11.16.
 */
public class CaptureMoveTest {


    private MoveReceiver moveReceiver = mock(MoveReceiver.class);
    private BoardCoordinate from = BoardCoordinate.A2;
    private BoardCoordinate to = BoardCoordinate.A5;
    private Piece capturedPiece = mock(Piece.class);

    private Move move = new CaptureMove(moveReceiver, from, to, capturedPiece);


    private InOrder inOrder = inOrder(moveReceiver);

    @Test
    public void playQuietMove() throws Exception {

        move.play();

        verify(moveReceiver).resetHalfMoveClock();
        inOrder.verify(moveReceiver).movePiece(from, to);
        inOrder.verify(moveReceiver).completePlay();
    }


    @Test
    public void undoQuietMove() throws Exception {

        move.undo();

        inOrder.verify(moveReceiver).movePiece(to, from);
        inOrder.verify(moveReceiver).put(to, capturedPiece);
        inOrder.verify(moveReceiver).completeUndo();
    }

}