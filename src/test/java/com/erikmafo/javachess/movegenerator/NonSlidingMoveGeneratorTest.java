package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.board.Offset;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.pieces.PieceColor;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 20.11.16.
 */
@RunWith(JUnitParamsRunner.class)
public class NonSlidingMoveGeneratorTest {

    private final Board board = mock(Board.class);
    private final MoveFactory moveFactory = mock(MoveFactory.class);

    @Before
    public void setUp() throws Exception {

        when(board.getColorToMove()).thenReturn(PieceColor.WHITE);
        when(board.pieceAt(any())).thenReturn(Optional.ofNullable(null));

        when(moveFactory.newQuietMove(any(BoardCoordinate.class), any(BoardCoordinate.class))).then(invocationOnMock -> {
            String name = new StringBuilder()
                    .append(invocationOnMock.getArgumentAt(0, BoardCoordinate.class))
                    .append(invocationOnMock.getArgumentAt(1, BoardCoordinate.class))
                    .toString();
            Move move = mock(Move.class, name);
            return move;
        });

    }


    public Object[] knightMovesTestFixtures() {

        return new Object[]{
                new Object[]{BoardCoordinate.C3, Offset.KNIGHT_LEAP_2DOWN_LEFT, BoardCoordinate.B1},
                new Object[]{BoardCoordinate.C3, Offset.KNIGHT_LEAP_2DOWN_RIGHT, BoardCoordinate.D1},
                new Object[]{BoardCoordinate.C3, Offset.KNIGHT_LEAP_2UP_LEFT, BoardCoordinate.B5},
                new Object[]{BoardCoordinate.C3, Offset.KNIGHT_LEAP_2UP_RIGHT, BoardCoordinate.D5},
                new Object[]{BoardCoordinate.C3, Offset.KNIGHT_LEAP_UP_2RIGHT, BoardCoordinate.E4}
        };
    }


    @Test
    @Parameters(method = "knightMovesTestFixtures")
    public void findKnightMoveOnEmtpyBoard(BoardCoordinate from, Offset offset, BoardCoordinate expectedTarget) throws Exception {

        boolean includeQuietMoves = true;
        NonSlidingMoveGenerator nonSlidingMoveGenerator = new NonSlidingMoveGenerator(moveFactory, includeQuietMoves, offset);
        Move move = mock(Move.class, "" + from + expectedTarget);
        when(moveFactory.newQuietMove(from, expectedTarget)).thenReturn(move);

        List<Move> moves = nonSlidingMoveGenerator.generateMoves(board, from);

        assertThat(moves, is(Arrays.asList(move)));
    }
}