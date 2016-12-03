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
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 20.11.16.
 */
public class SlidingMoveGeneratorTest {

    private final Board board = mock(Board.class);
    private final MoveFactory moveFactory = mock(MoveFactory.class);

    @Before
    public void setUp() throws Exception {

        when(board.getColorToMove()).thenReturn(PieceColor.WHITE);
        when(board.pieceAt(any())).thenReturn(Optional.ofNullable(null));

    }

    @Test
    public void findRookMovesOnEmptyBoard() throws Exception {

        BoardCoordinate from = BoardCoordinate.E4;
        Offset slidingDirection = Offset.DOWN;

        SlidingMoveGenerator slidingMoveGenerator = new SlidingMoveGenerator(moveFactory, slidingDirection);

        Move e4e3 = mock(Move.class, "e4e3");
        Move e4e2 = mock(Move.class, "e4e2");
        Move e4e1 = mock(Move.class, "e4e1");

        when(moveFactory.newQuietMove(from, BoardCoordinate.E3)).thenReturn(e4e3);
        when(moveFactory.newQuietMove(from, BoardCoordinate.E2)).thenReturn(e4e2);
        when(moveFactory.newQuietMove(from, BoardCoordinate.E1)).thenReturn(e4e1);

        List<Move> moves = slidingMoveGenerator.generateMoves(board, from);

        assertThat(moves, is(Arrays.asList(e4e3, e4e2, e4e1)));
    }

    @Test
    public void findBishopMovesOnEmptyBoard() throws Exception {

        BoardCoordinate from = BoardCoordinate.E4;
        Offset slidingDirection = Offset.UP_LEFT;

        SlidingMoveGenerator slidingMoveGenerator = new SlidingMoveGenerator(moveFactory, slidingDirection);

        Move e4d5 = mock(Move.class, "e4d5");
        Move e4c6 = mock(Move.class, "e4c6");
        Move e4b7 = mock(Move.class, "e4b7");
        Move e4a8 = mock(Move.class, "e4a8");

        when(moveFactory.newQuietMove(from, BoardCoordinate.D5)).thenReturn(e4d5);
        when(moveFactory.newQuietMove(from, BoardCoordinate.C6)).thenReturn(e4c6);
        when(moveFactory.newQuietMove(from, BoardCoordinate.B7)).thenReturn(e4b7);
        when(moveFactory.newQuietMove(from, BoardCoordinate.A8)).thenReturn(e4a8);

        List<Move> moves = slidingMoveGenerator.generateMoves(board, from);

        assertThat(moves, is(Arrays.asList(e4d5, e4c6, e4b7, e4a8)));
    }




}