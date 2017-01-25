package com.erikmafo.chess.board;

import com.erikmafo.chess.movegenerator.MoveGeneratorFactory;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import com.erikmafo.chess.testingutils.PieceMocks;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by erikmafo on 19.11.16.
 */
public class BoardImplTest {



    private ZobristCalculator zobristCalculator = mock(ZobristCalculator.class);

    private BoardImpl board = new BoardImpl(zobristCalculator);

    private Piece piece = mock(Piece.class);


    @Test
    public void updateZobristWhenEnPassentTargetIsSet() throws Exception {

        
        Square square = Square.C3;

        board.setEnPassentTarget(square);

        verify(zobristCalculator).shiftEnPassentTarget(square);
        verifyNoMoreInteractions(zobristCalculator);
    }


    @Test
    public void updateZobristWhenEnPassentTargetIsRemoved() throws Exception {

        Square square = Square.C3;

        board.setEnPassentTarget(square);
        board.removeEnPassentTarget();

        verify(zobristCalculator, times(2)).shiftEnPassentTarget(square);
    }

    @Test
    public void updateZobristWhenCompletePlay() throws Exception {

        board.completePlay();

        verify(zobristCalculator).shiftColorToMove();
        verifyNoMoreInteractions(zobristCalculator);
    }

    @Test
    public void updateZobristWhenCompleteUndo() throws Exception {

        board.completeUndo();

        verify(zobristCalculator).shiftColorToMove();
        verifyNoMoreInteractions(zobristCalculator);
    }

    @Test
    public void updateZobristWhenMovingPiece() throws Exception {

        Piece piece = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.PAWN);
        Square from = Square.F3;
        Square to = Square.F4;

        board.put(from, piece);
        board.movePiece(from, to);

        verify(zobristCalculator, times(2)).shiftPiece(from , piece);
        verify(zobristCalculator).shiftPiece(to, piece);
        verifyNoMoreInteractions(zobristCalculator);
    }


    @Test
    public void removeEnPassentShouldNotCauseNullPointerException() throws Exception {

        board.removeEnPassentTarget();

        verify(zobristCalculator, never()).shiftEnPassentTarget(null);

    }

    @Test
    public void getOccupiedSquaresShouldReturnEmptyListWhenBoardIsEmpty() throws Exception {
        assertThat(new HashSet<>(board.getOccupiedSquares()), is(Collections.emptySet()));
    }

    @Test
    public void getOccupiedSquares() throws Exception {

        List<Square> squares = Arrays.asList(Square.A3, Square.G6, Square.B3);

        for (Square square : squares) {
            board.put(square, mock(Piece.class));
        }

        assertThat(new HashSet<>(board.getOccupiedSquares()), is(new HashSet<>(squares)));

    }

    @Test
    public void putAndRemove() throws Exception {

        Square square = Square.G6;

        board.put(square, piece);

        assertThat(board.isOccupied(square), is(true));
        assertThat(board.getNullablePiece(square), is(piece));

        board.remove(square);

        assertThat(board.isOccupied(square), is(false));
    }


    @Test
    public void movePiece() throws Exception {

        Square from = Square.G6;
        Square to = Square.F6;

        board.put(from, piece);

        board.movePiece(from, to);

        assertThat(board.isOccupied(from), is(false));
        assertThat(board.isOccupied(to), is(true));
        assertThat(board.getNullablePiece(to), is(piece));
    }


    @Test
    public void shouldKeepEnPassentTargetOnlyForNextTurn() throws Exception {

        Square square = Square.A3;

        board.setEnPassentTarget(square);

        assertThat("En passent target should not be active before next turn",
                board.enPassentTarget().isPresent(), is(false));

        board.completePlay();

        assertThat("Should activate the en passent target on next turn",
                board.enPassentTarget().isPresent(), is(true));

        board.completePlay();

        assertThat("Should remove en passent target after next turn completes",
                board.enPassentTarget().isPresent(), is(false));
    }


    @Test
    public void shouldKeepEnPassentTargetForCurrentTurn() throws Exception {

        Square square = Square.A3;

        board.setEnPassentTarget(square);

        board.completePlay();

        board.removeEnPassentTarget();

        assertThat("Should not remove en passent target for the current turn",
                board.enPassentTarget().isPresent(), is(true));

    }



    @Test
    public void shouldHaveCastlingIfKingAndRookHasNotMoved() throws Exception {

        Piece king = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KING);
        Piece rook = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.ROOK);

        board.put(Square.E1, king);
        board.put(Square.G1, mock(Piece.class));
        board.put(Square.F1, mock(Piece.class));
        board.put(Square.H1, rook);

        assertThat(board.hasKingSideCastlingRight(PieceColor.WHITE), is(true));
    }


    @Test
    public void shouldLooseCastlingRightIfRookMoves() throws Exception {

        Piece king = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KING);
        Piece rook = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.ROOK);

        board.put(Square.E1, king);
        board.put(Square.H1, rook);

        board.movePiece(Square.H1, Square.H2);
        board.completePlay();

        boolean actual = board.hasKingSideCastlingRight(PieceColor.WHITE);

        assertThat(actual, is(false));

    }


    @Test
    public void shouldLooseCastlingRightIfKingMoves() throws Exception {

        Piece king = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KING);
        Piece rook = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.ROOK);

        board.put(Square.E1, king);
        board.put(Square.H1, rook);

        board.movePiece(Square.E1, Square.E2);
        board.completePlay();

        boolean actual = board.hasKingSideCastlingRight(PieceColor.WHITE);

        assertThat(actual, is(false));

    }


    @Test
    public void shouldRegainCaslingRightAfterUndo() throws Exception {

        Piece king = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KING);
        Piece rook = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.ROOK);

        board.put(Square.E1, king);
        board.put(Square.H1, rook);

        board.movePiece(Square.E1, Square.E2);
        board.completePlay();

        board.movePiece(Square.E2, Square.E1);
        board.completeUndo();

        boolean actual = board.hasKingSideCastlingRight(PieceColor.WHITE);

        assertThat(actual, is(true));

    }

    @Test
    public void getKingLocation() throws Exception {


        Piece blackKing = PieceMocks.newPieceMock(PieceColor.BLACK, PieceType.KING);
        Piece blackPawn = PieceMocks.newPieceMock(PieceColor.BLACK, PieceType.PAWN);

        Piece whiteKing = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KING);

        Square kingLocation = Square.E8;
        board.put(kingLocation, blackKing);
        board.put(Square.E7, blackPawn);

        board.put(Square.E1, whiteKing);

        assertThat(board.getKingLocation(PieceColor.BLACK), is(kingLocation));


    }
}