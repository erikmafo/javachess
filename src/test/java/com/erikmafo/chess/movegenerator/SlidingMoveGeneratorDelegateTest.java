package com.erikmafo.chess.movegenerator;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.BasicOffset;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.board.Offset;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.move.MoveFactory;
import com.erikmafo.chess.pieces.PieceColor;
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
public class SlidingMoveGeneratorDelegateTest {

    private final Board board = mock(Board.class);
    private final MoveFactory moveFactory = mock(MoveFactory.class);

    @Before
    public void setUp() throws Exception {

        when(board.getColorToMove()).thenReturn(PieceColor.WHITE);
        when(board.pieceAt(any())).thenReturn(Optional.ofNullable(null));

    }

    @Test
    public void findRookMovesOnEmptyBoard() throws Exception {

        Square from = Square.E4;
        Offset slidingDirection = BasicOffset.DOWN;

        SlidingMoveGeneratorDelegate slidingMoveGenerator = new SlidingMoveGeneratorDelegate(moveFactory, slidingDirection);

        Move e4e3 = mock(Move.class, "e4e3");
        Move e4e2 = mock(Move.class, "e4e2");
        Move e4e1 = mock(Move.class, "e4e1");

        when(moveFactory.newQuietMove(board, from, Square.E3)).thenReturn(e4e3);
        when(moveFactory.newQuietMove(board, from, Square.E2)).thenReturn(e4e2);
        when(moveFactory.newQuietMove(board, from, Square.E1)).thenReturn(e4e1);

        List<Move> moves = slidingMoveGenerator.generateMoves(board, from);

        assertThat(moves, is(Arrays.asList(e4e3, e4e2, e4e1)));
    }

    @Test
    public void findBishopMovesOnEmptyBoard() throws Exception {

        Square from = Square.E4;
        Offset slidingDirection = BasicOffset.UP_LEFT;

        SlidingMoveGeneratorDelegate slidingMoveGenerator = new SlidingMoveGeneratorDelegate(moveFactory, slidingDirection);

        Move e4d5 = mock(Move.class, "e4d5");
        Move e4c6 = mock(Move.class, "e4c6");
        Move e4b7 = mock(Move.class, "e4b7");
        Move e4a8 = mock(Move.class, "e4a8");

        when(moveFactory.newQuietMove(board, from, Square.D5)).thenReturn(e4d5);
        when(moveFactory.newQuietMove(board, from, Square.C6)).thenReturn(e4c6);
        when(moveFactory.newQuietMove(board, from, Square.B7)).thenReturn(e4b7);
        when(moveFactory.newQuietMove(board, from, Square.A8)).thenReturn(e4a8);

        List<Move> moves = slidingMoveGenerator.generateMoves(board, from);

        assertThat(moves, is(Arrays.asList(e4d5, e4c6, e4b7, e4a8)));
    }




}