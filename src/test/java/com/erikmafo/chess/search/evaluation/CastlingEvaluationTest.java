package com.erikmafo.chess.search.evaluation;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.pieces.PieceColor;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 05.12.16.
 */
@RunWith(JUnitParamsRunner.class)
public class CastlingEvaluationTest {

    private Board board = mock(Board.class);

    private int castledValue = 3;
    private int kingSideCastlingRightValue = 2;
    private int queenSideCastlingRightValue = 1;

    private CastlingEvaluation castlingEvaluation = new CastlingEvaluation(
            castledValue, kingSideCastlingRightValue, queenSideCastlingRightValue);


    private Object[] castlingFixtures() {
        return new Object[]{
                new Object[]{PieceColor.WHITE, false, true, -castledValue},
                new Object[]{PieceColor.WHITE, true, false, castledValue},
                new Object[]{PieceColor.WHITE, true, true, 0},
                new Object[]{PieceColor.WHITE, false, false, 0}
        };
    }


    @Test
    @Parameters(method = "castlingFixtures")
    public void applyAsInt(PieceColor colorToMove, boolean hasCastled, boolean hasOpponentCastled, int expectedValue) throws Exception {

        when(board.getColorToMove()).thenReturn(colorToMove);

        when(board.hasCastled(colorToMove)).thenReturn(hasCastled);
        when(board.hasCastled(colorToMove.getOpposite())).thenReturn(hasOpponentCastled);

        assertEquals(castlingEvaluation.applyAsInt(board), expectedValue);
    }
}