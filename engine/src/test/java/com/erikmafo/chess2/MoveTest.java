package com.erikmafo.chess2;

import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import com.erikmafo.chess2.movegeneration.Move;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class MoveTest {


    private Object[][] compareMoves_TestCases() {


        return new Object[][]{
                new Object[]{
                        new Move(PieceColor.WHITE, PieceType.ROOK, Move.Kind.CAPTURE, Square.D2, Square.D4, PieceType.KNIGHT),
                        new Move(PieceColor.WHITE, PieceType.ROOK, Move.Kind.QUIET, Square.D2, Square.D4, null),
                        1
                }
        };

    }


    @Test
    @Parameters(method = "compareMoves_TestCases")
    public void testCompareMoves(Move move1, Move move2, int expected) throws Exception {

        int sign = (int) Math.signum(move1.compareTo(move2));

        assertThat(sign, is(expected));
    }
}