package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.pieces.PieceColor;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 19.11.16.
 */
public class CastlingMoveTest {

    private final BoardCoordinate kingFrom = BoardCoordinate.E1;
    private final BoardCoordinate kingTo = BoardCoordinate.G1;
    private final BoardCoordinate rookFrom = BoardCoordinate.H1;
    private final BoardCoordinate rookTo = BoardCoordinate.F1;

    private final PieceColor color = PieceColor.WHITE;

    private final MoveReceiver moveReceiver = mock(MoveReceiver.class);

    private final Move castlingMove = new CastlingMove(moveReceiver, kingFrom, kingTo, rookFrom, rookTo);

    @Test
    public void playCastlingMove() throws Exception {

        castlingMove.play();

        verify(moveReceiver).movePiece(kingFrom, kingTo);
        verify(moveReceiver).movePiece(rookFrom, rookTo);
        verify(moveReceiver).setHasCastled(color, true);
        verify(moveReceiver).completePlay();

    }

    @Test
    public void undoCastlingMove() throws Exception {

        castlingMove.undo();

        verify(moveReceiver).movePiece(kingTo, kingFrom);
        verify(moveReceiver).movePiece(rookTo, rookFrom);
        verify(moveReceiver).setHasCastled(color, false);
        verify(moveReceiver).completeUndo();

    }
}