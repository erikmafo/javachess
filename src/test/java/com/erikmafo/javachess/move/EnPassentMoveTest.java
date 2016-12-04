package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.pieces.Piece;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 18.11.16.
 */
public class EnPassentMoveTest {

    private MoveReceiver moveReceiver = mock(MoveReceiver.class);
    private Square from = Square.D6;
    private Square to = Square.C5;
    private Square captureCoordinate = Square.C6;
    private Piece capturedPiece = mock(Piece.class);

    private EnPassentMove enPassentMove = new EnPassentMove(moveReceiver, from, to, capturedPiece);

    @Test
    public void executeEnPassentMoveCorrectly() throws Exception {

        enPassentMove.play();

        verify(moveReceiver).movePiece(from, to);
        verify(moveReceiver).remove(captureCoordinate);
        verify(moveReceiver).resetHalfMoveClock();
        verify(moveReceiver).completePlay();
    }


    @Test
    public void rewindEnPassentMoveCorrectly() throws Exception {

        enPassentMove.undo();

        verify(moveReceiver).movePiece(to, from);
        verify(moveReceiver).put(captureCoordinate, capturedPiece);
        verify(moveReceiver).completeUndo();

    }

}