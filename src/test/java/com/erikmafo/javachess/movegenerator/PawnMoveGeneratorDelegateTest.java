package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 20.11.16.
 */
public class PawnMoveGeneratorDelegateTest {


    private MoveFactory moveFactory = mock(MoveFactory.class);

    private MoveGeneratorDelegate pawnMoveGeneratorDelegate = new PawnMoveGeneratorDelegate(moveFactory);

    private Board board = mock(Board.class);


    @Before
    public void setUp() throws Exception {

        when(board.pieceAt(any())).thenReturn(Optional.ofNullable(null));
        when(board.enPassentTarget()).thenReturn(Optional.ofNullable(null));

    }

    @Test
    public void findEnPassentMoveWhenEnPassentTargetIsSet() throws Exception {

        PieceColor colorToMove = PieceColor.WHITE;
        Square from = Square.B5;
        Square enPassentTarget = Square.A6;
        Square capturePieceSquare = Square.A5;
        when(board.enPassentTarget()).thenReturn(Optional.ofNullable(enPassentTarget));

        when(board.getColorToMove()).thenReturn(colorToMove);

        Piece captured = mock(Piece.class);
        when(board.pieceAt(capturePieceSquare)).thenReturn(Optional.ofNullable(captured));

        Move expected = mock(Move.class, "" + from + enPassentTarget);
        when(moveFactory.newEnPassentMove(board, from, enPassentTarget, captured)).thenReturn(expected);

        List<Move> moves = pawnMoveGeneratorDelegate.generateMoves(board, from);

        assertThat(moves, hasItem(expected));
    }


    @Test
    public void onlyFindEnPasssentMoveFromValidRank() throws Exception {

        PieceColor colorToMove = PieceColor.WHITE;
        Square from = Square.B2;
        Square enPassentTarget = Square.A6;
        Square oppentPawnSquare = Square.A5;
        when(board.enPassentTarget()).thenReturn(Optional.ofNullable(enPassentTarget));

        when(board.getColorToMove()).thenReturn(colorToMove);

        Piece opponentPawn = mock(Piece.class);
        when(board.pieceAt(oppentPawnSquare)).thenReturn(Optional.ofNullable(opponentPawn));

        Move enPassentMove = mock(Move.class, "" + from + enPassentTarget);
        when(moveFactory.newEnPassentMove(board, from, enPassentTarget, opponentPawn)).thenReturn(enPassentMove);

        List<Move> moves = pawnMoveGeneratorDelegate.generateMoves(board, from);

        assertThat(moves, not(hasItem(enPassentMove)));

    }

    @Test
    public void findPawnPushMoves() throws Exception {

        PieceColor colorToMove = PieceColor.WHITE;
        when(board.getColorToMove()).thenReturn(colorToMove);
        Square from = Square.E2;

        Square oneUp = Square.E3;
        Square twoUp = Square.E4;

        Move singlePush = mock(Move.class, "" + from + oneUp);
        Move doublePush = mock(Move.class, "" + from + twoUp);

        when(moveFactory.newSinglePawnPushMove(board, from, oneUp)).thenReturn(singlePush);
        when(moveFactory.newDoublePawnPushMove(board, from, twoUp)).thenReturn(doublePush);

        List<Move> moves = pawnMoveGeneratorDelegate.generateMoves(board, from);

        assertThat(moves, is(Arrays.asList(singlePush, doublePush)));
    }




}