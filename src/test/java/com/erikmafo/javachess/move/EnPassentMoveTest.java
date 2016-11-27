package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.pieces.Piece;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 18.11.16.
 */
public class EnPassentMoveTest {

    private MoveReceiver moveReceiver = mock(MoveReceiver.class);
    private BoardCoordinate from = BoardCoordinate.D6;
    private BoardCoordinate to = BoardCoordinate.C5;
    private BoardCoordinate captureCoordinate = BoardCoordinate.C6;
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