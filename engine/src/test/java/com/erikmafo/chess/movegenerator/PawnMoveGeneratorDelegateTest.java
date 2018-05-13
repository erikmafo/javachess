package com.erikmafo.chess.movegenerator;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.move.MoveFactory;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import com.erikmafo.chess.testingutils.PieceMocks;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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


@RunWith(JUnitParamsRunner.class)
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
    @Parameters({
            "WHITE, B5, A6, A5",
            "BLACK, B4, A3, A4"
    })
    public void findEnPassentMoveWhenEnPassentTargetIsSet(
            PieceColor colorToMove, Square from, Square enPassentTarget, Square capturePieceSquare) throws Exception {

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
    public void shouldOnlyFindEnPasssentMoveFromValidRank() throws Exception {

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
    @Parameters({
            "WHITE, E2, E3, E4",
            "BLACK, E7, E6, E5"})
    public void shouldFindDoublePawnPushFromInitialSquare(PieceColor colorToMove, Square from, Square oneUp, Square twoUp) throws Exception {

        when(board.getColorToMove()).thenReturn(colorToMove);

        Move singlePush = mock(Move.class, "" + from + oneUp);
        Move doublePush = mock(Move.class, "" + from + twoUp);

        when(moveFactory.newSinglePawnPushMove(board, from, oneUp)).thenReturn(singlePush);
        when(moveFactory.newDoublePawnPushMove(board, from, twoUp)).thenReturn(doublePush);

        List<Move> moves = pawnMoveGeneratorDelegate.generateMoves(board, from);

        assertThat(moves, is(Arrays.asList(singlePush, doublePush)));
    }

    @Test
    @Parameters({
            "WHITE, E3, D4",
            "WHITE, E3, F4",
            "BLACK, E6, D5",
            "BLACK, E6, F5"
    })
    public void shouldFindWhitePawnAttackMoves(
            PieceColor pawnColor, Square from, Square opponentSquare) throws Exception {

        Piece pawn = PieceMocks.newPieceMock(pawnColor, PieceType.PAWN);
        Piece opponent = PieceMocks.newPieceMock(pawnColor.opponent(), PieceType.BISHOP);

        when(board.getColorToMove()).thenReturn(pawnColor);
        when(board.getNullablePiece(from)).thenReturn(pawn);
        when(board.getNullablePiece(opponentSquare)).thenReturn(opponent);

        Move attackMove = mock(Move.class, "pawn attack move");
        when(moveFactory.newCaptureMove(board, from, opponentSquare, opponent)).thenReturn(attackMove);

        List<Move> moves = pawnMoveGeneratorDelegate.generateMoves(board, from);

        assertThat(moves, hasItem(attackMove));
    }
}