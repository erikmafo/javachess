package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.board.Offset;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.pieces.PieceColor;
import org.junit.Before;
import org.junit.Test;

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
public class NonSlidingMoveGeneratorTest {

    private final Board board = mock(Board.class);
    private final MoveFactory moveFactory = mock(MoveFactory.class);

    @Before
    public void setUp() throws Exception {

        when(board.getColorToMove()).thenReturn(PieceColor.WHITE);
        when(board.pieceAt(any())).thenReturn(Optional.ofNullable(null));

    }
    @Test
    public void findKnightMoveOnEmtpyBoard() throws Exception {

        boolean includeQuietMoves = true;
        Offset offset = Offset.KNIGHT_LEAP_2DOWN_LEFT;
        NonSlidingMoveGenerator nonSlidingMoveGenerator = new NonSlidingMoveGenerator(includeQuietMoves, offset);
        BoardCoordinate from = BoardCoordinate.C3;
        BoardCoordinate expectedTarget = BoardCoordinate.B1;
        Move move = mock(Move.class, "" + from + expectedTarget);
        when(moveFactory.newQuietMove(from, expectedTarget)).thenReturn(move);

        List<Move> moves = nonSlidingMoveGenerator.generateMoves(board, from, moveFactory);

        assertThat(moves, is(Arrays.asList(move)));
    }
}