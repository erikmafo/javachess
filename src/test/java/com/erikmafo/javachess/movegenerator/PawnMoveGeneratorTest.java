package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import org.hamcrest.CoreMatchers;
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
public class PawnMoveGeneratorTest {


    private MoveGenerator pawnMoveGenerator = new PawnMoveGenerator();

    private Board board = mock(Board.class);

    private MoveFactory moveFactory = mock(MoveFactory.class);

    @Before
    public void setUp() throws Exception {

        when(board.pieceAt(any())).thenReturn(Optional.ofNullable(null));
        when(board.enPassentTarget()).thenReturn(Optional.ofNullable(null));

    }

    @Test
    public void findEnPassentMoveWhenEnPassentTargetIsSet() throws Exception {

        PieceColor colorToMove = PieceColor.WHITE;
        BoardCoordinate from = BoardCoordinate.B5;
        BoardCoordinate enPassentTarget = BoardCoordinate.A6;
        BoardCoordinate capturePieceSquare = BoardCoordinate.A5;
        when(board.enPassentTarget()).thenReturn(Optional.ofNullable(enPassentTarget));

        when(board.getColorToMove()).thenReturn(colorToMove);

        Piece captured = mock(Piece.class);
        when(board.pieceAt(capturePieceSquare)).thenReturn(Optional.ofNullable(captured));

        Move expected = mock(Move.class, "" + from + enPassentTarget);
        when(moveFactory.newEnPassentMove(from, enPassentTarget, captured)).thenReturn(expected);

        List<Move> moves = pawnMoveGenerator.generateMoves(board, from, moveFactory);

        assertThat(moves, CoreMatchers.hasItem(expected));
    }


    @Test
    public void findPawnPushMoves() throws Exception {

        PieceColor colorToMove = PieceColor.WHITE;
        when(board.getColorToMove()).thenReturn(colorToMove);
        BoardCoordinate from = BoardCoordinate.E2;

        BoardCoordinate oneUp = BoardCoordinate.E3;
        BoardCoordinate twoUp = BoardCoordinate.E4;

        Move singlePush = mock(Move.class, "" + from + oneUp);
        Move doublePush = mock(Move.class, "" + from + twoUp);

        when(moveFactory.newSinglePawnPushMove(from, oneUp)).thenReturn(singlePush);
        when(moveFactory.newDoublePawnPushMove(from, twoUp)).thenReturn(doublePush);

        List<Move> moves = pawnMoveGenerator.generateMoves(board, from, moveFactory);

        assertThat(moves, is(Arrays.asList(singlePush, doublePush)));
    }




}