package com.erikmafo.chess.movegenerator;

import com.erikmafo.chess.board.*;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.move.MoveFactory;
import com.erikmafo.chess.pieces.PieceColor;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 20.11.16.
 */
@RunWith(JUnitParamsRunner.class)
public class NonSlidingMoveGeneratorDelegateTest {

    private final Board board = mock(Board.class);
    private final MoveFactory moveFactory = mock(MoveFactory.class);

    @Before
    public void setUp() throws Exception {

        when(board.getColorToMove()).thenReturn(PieceColor.WHITE);
        when(board.pieceAt(any())).thenReturn(Optional.ofNullable(null));

        when(moveFactory.newQuietMove(argThat(is(board)), any(Square.class), any(Square.class))).then(invocationOnMock -> {
            String name = new StringBuilder()
                    .append(invocationOnMock.getArgumentAt(1, Square.class))
                    .append(invocationOnMock.getArgumentAt(2, Square.class))
                    .toString();
            Move move = mock(Move.class, name);
            return move;
        });

    }


    public Object[] knightMovesTestFixtures() {

        return new Object[]{
                new Object[]{Square.C3, KnightOffset.KNIGHT_LEAP_2DOWN_LEFT, Square.B1},
                new Object[]{Square.C3, KnightOffset.KNIGHT_LEAP_2DOWN_RIGHT, Square.D1},
                new Object[]{Square.C3, KnightOffset.KNIGHT_LEAP_2UP_LEFT, Square.B5},
                new Object[]{Square.C3, KnightOffset.KNIGHT_LEAP_2UP_RIGHT, Square.D5},
                new Object[]{Square.C3, KnightOffset.KNIGHT_LEAP_UP_2RIGHT, Square.E4}
        };
    }


    @Test
    @Parameters(method = "knightMovesTestFixtures")
    public void findKnightMoveOnEmtpyBoard(Square from, Offset offset, Square expectedTarget) throws Exception {

        boolean includeQuietMoves = true;
        NonSlidingMoveGeneratorDelegate nonSlidingMoveGenerator = new NonSlidingMoveGeneratorDelegate(moveFactory, includeQuietMoves, offset);
        Move move = mock(Move.class, "" + from + expectedTarget);
        when(moveFactory.newQuietMove(board, from, expectedTarget)).thenReturn(move);

        List<Move> moves = nonSlidingMoveGenerator.generateMoves(board, from);

        assertThat(moves, is(Arrays.asList(move)));
    }
}