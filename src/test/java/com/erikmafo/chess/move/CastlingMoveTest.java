package com.erikmafo.chess.move;

import com.erikmafo.chess.board.MoveReceiver;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.PieceColor;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 19.11.16.
 */
public class CastlingMoveTest {

    private final Square kingFrom = Square.E1;
    private final Square kingTo = Square.G1;
    private final Square rookFrom = Square.H1;
    private final Square rookTo = Square.F1;

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