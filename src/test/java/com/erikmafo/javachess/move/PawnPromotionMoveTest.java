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
 * Created by erikmafo on 20.11.16.
 */
public class PawnPromotionMoveTest {


    private final MoveReceiver moveReceiver = mock(MoveReceiver.class);

    private final Piece pawn = mock(Piece.class);
    private final Piece promoteTo = mock(Piece.class);

    private final BoardCoordinate from = BoardCoordinate.E7;
    private final BoardCoordinate to = BoardCoordinate.E8;

    private final Move move = new PawnPromotionMove(moveReceiver, from, to, pawn, promoteTo);

    private final InOrder inOrder = inOrder(moveReceiver);

    @Test
    public void play() throws Exception {

        move.play();

        inOrder.verify(moveReceiver).movePiece(from, to);
        inOrder.verify(moveReceiver).put(to, promoteTo);
        inOrder.verify(moveReceiver).completePlay();

    }

    @Test
    public void undo() throws Exception {

        move.undo();

        inOrder.verify(moveReceiver).movePiece(to, from);
        inOrder.verify(moveReceiver).put(from, pawn);
        inOrder.verify(moveReceiver).completeUndo();
    }

}