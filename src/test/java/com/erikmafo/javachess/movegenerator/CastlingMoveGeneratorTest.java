package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BoardCoordinate;
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
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 20.11.16.
 */
@RunWith(JUnitParamsRunner.class)
public class CastlingMoveGeneratorTest {


    private final Board board = mock(Board.class);
    private final MoveFactory moveFactory = mock(MoveFactory.class);

    private final BoardSeeker boardSeeker = mock(BoardSeeker.class);
    private final CastlingMoveGenerator castlingMoveGenerator = new CastlingMoveGenerator(moveFactory, boardSeeker);

    @Before
    public void setUp() throws Exception {
        when(board.pieceAt(any())).thenReturn(Optional.ofNullable(null));
    }


    private Object[] kingSideCastlingFixtures() {
        return new Object[]{
                new Object[]{PieceColor.WHITE, BoardCoordinate.E1, BoardCoordinate.G1, BoardCoordinate.H1, BoardCoordinate.F1},
                new Object[]{PieceColor.BLACK, BoardCoordinate.E8, BoardCoordinate.G8, BoardCoordinate.H8, BoardCoordinate.F8}
        };
    }



    @Test
    @Parameters(method = "kingSideCastlingFixtures")
    public void findKingSideCastlingMove(
            PieceColor color, BoardCoordinate kingFrom, BoardCoordinate kingTo,
            BoardCoordinate rookFrom, BoardCoordinate rookTo) throws Exception {

        PieceColor opponent = color.getOpposite();

        when(board.getColorToMove()).thenReturn(color);
        when(board.hasKingSideCastlingRight(color)).thenReturn(true);

        when(boardSeeker.isAttackedBy(color, kingFrom, board)).thenReturn(true);
        when(boardSeeker.isAttackedBy(color, rookTo, board)).thenReturn(true);
        when(boardSeeker.isAttackedBy(color, kingTo, board)).thenReturn(true);

        when(boardSeeker.isAttackedBy(opponent, kingFrom, board)).thenReturn(false);
        when(boardSeeker.isAttackedBy(opponent, rookTo, board)).thenReturn(false);
        when(boardSeeker.isAttackedBy(opponent, kingTo, board)).thenReturn(false);
        when(boardSeeker.isAttackedBy(opponent, rookFrom, board)).thenReturn(false);

        Move kingSideCastlingMove = mock(Move.class, "O-O");
        when(moveFactory.newCastlingMove(kingFrom, kingTo, rookFrom, rookTo)).thenReturn(kingSideCastlingMove);

        List<Move> moves = castlingMoveGenerator.generateMoves(board, kingFrom);

        assertThat(moves, is(Arrays.asList(kingSideCastlingMove)));
    }










}