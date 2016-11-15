package com.erikmafo.javachess.moves;

import com.erikmafo.javachess.boardrep.Board;
import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.MoveTarget;
import com.erikmafo.javachess.pieces.PieceType;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 15.11.16.
 */
public class PawnPromotionMoveTest {


    private MoveTarget moveTarget = mock(MoveTarget.class);

    private BoardCoordinate from = BoardCoordinate.A2;
    private BoardCoordinate to = BoardCoordinate.A1;
    private PieceType promoteTo = PieceType.QUEEN;

    private Move move = new PawnPromotionMove(from, to, promoteTo);


    @Test
    public void executePromotionMoveCorrectly() throws Exception {

        move.execute(moveTarget);

        verify(moveTarget).movePiece(from, to);
        verify(moveTarget).promotePawn(to, promoteTo);
    }

    @Test
    public void rewindPropmotionMoveCorrectly() throws Exception {

        move.rewind(moveTarget);

        verify(moveTarget).movePieceBackwards(to, from);
        verify(moveTarget).promotePawn(from, PieceType.PAWN);
    }
}