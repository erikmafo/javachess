package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.pieces.Piece;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 18.11.16.
 */
@RunWith(JUnitParamsRunner.class)
public class EnPassentMoveTest {

    private MoveReceiver moveReceiver = mock(MoveReceiver.class);
    private Piece capturedPiece = mock(Piece.class);


    private Object[] enPassentMoveFixtures() {

        return new Object[]{
                new Object[]{Square.C5, Square.D6, Square.D5},
                new Object[] {Square.A5, Square.B6, Square.B5},
                new Object[] {Square.H5, Square.G6, Square.G5},
                new Object[] {Square.E4, Square.D3, Square.D4}
        };
    }

    @Test
    @Parameters(method = "enPassentMoveFixtures")
    public void executeEnPassentMoveCorrectly(Square from, Square to, Square captureCoordinate) throws Exception {

        EnPassentMove enPassentMove = new EnPassentMove(moveReceiver, from, to, capturedPiece);

        enPassentMove.play();

        verify(moveReceiver).movePiece(from, to);
        verify(moveReceiver).remove(captureCoordinate);
        verify(moveReceiver).resetHalfMoveClock();
        verify(moveReceiver).completePlay();
    }


    @Test
    @Parameters(method = "enPassentMoveFixtures")
    public void rewindEnPassentMoveCorrectly(Square from, Square to, Square captureCoordinate) throws Exception {

        EnPassentMove enPassentMove = new EnPassentMove(moveReceiver, from, to, capturedPiece);

        enPassentMove.undo();

        verify(moveReceiver).movePiece(to, from);
        verify(moveReceiver).put(captureCoordinate, capturedPiece);
        verify(moveReceiver).completeUndo();

    }

}