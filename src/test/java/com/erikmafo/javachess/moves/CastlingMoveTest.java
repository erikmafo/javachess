package com.erikmafo.javachess.moves;

import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.MoveTarget;
import com.erikmafo.javachess.pieces.PieceColor;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by erikmafo on 13.11.16.
 */
public class CastlingMoveTest {

    private BoardCoordinate kingFrom = BoardCoordinate.E1;
    private BoardCoordinate kingTo = BoardCoordinate.G1;
    private BoardCoordinate rookFrom = BoardCoordinate.H1;
    private BoardCoordinate rookTo = BoardCoordinate.F1;

    private PieceColor color = PieceColor.WHITE;

    private CastlingMove castlingMove = new CastlingMove(kingFrom, kingTo, rookFrom, rookTo);

    private MoveTarget moveTarget = mock(MoveTarget.class);

    @Test
    public void executeCastlingMoveCorrectly() throws Exception {

        castlingMove.execute(moveTarget);

        verify(moveTarget).movePiece(kingFrom, kingTo);
        verify(moveTarget).movePiece(rookFrom, rookTo);
        verify(moveTarget).setHasCastled(color, true);
    }


    @Test
    public void rewindCastlingMoveCorrectly() throws Exception {

        castlingMove.rewind(moveTarget);

        verify(moveTarget).movePieceBackwards(kingTo, kingFrom);
        verify(moveTarget).movePieceBackwards(rookTo, rookFrom);
        verify(moveTarget).setHasCastled(color, false);
    }



}