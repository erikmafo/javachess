package com.erikmafo.chess.move;

import com.erikmafo.chess.board.MoveReceiver;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;
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
    private Square from = Square.A2;
    private Square to = Square.A5;
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