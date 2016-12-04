package com.erikmafo.javachess.board;

import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;
import com.erikmafo.javachess.testingutils.PieceMocks;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 19.11.16.
 */
public class BoardImplTest {


    private BoardImpl board = new BoardImpl();

    private Piece piece = mock(Piece.class);


    @Test
    public void putAndRemove() throws Exception {

        BoardCoordinate square = BoardCoordinate.G6;

        board.put(square, piece);

        assertThat(board.isOccupied(square), is(true));
        assertThat(board.getNullablePiece(square), is(piece));

        board.remove(square);

        assertThat(board.isOccupied(square), is(false));
    }


    @Test
    public void movePiece() throws Exception {

        BoardCoordinate from = BoardCoordinate.G6;
        BoardCoordinate to = BoardCoordinate.F6;

        board.put(from, piece);

        board.movePiece(from, to);

        assertThat(board.isOccupied(from), is(false));
        assertThat(board.isOccupied(to), is(true));
        assertThat(board.getNullablePiece(to), is(piece));
    }


    @Test
    public void shouldKeepEnPassentTargetOnlyForNextTurn() throws Exception {

        BoardCoordinate square = BoardCoordinate.A3;

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

        BoardCoordinate square = BoardCoordinate.A3;

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

        board.put(BoardCoordinate.E1, king);
        board.put(BoardCoordinate.G1, mock(Piece.class));
        board.put(BoardCoordinate.F1, mock(Piece.class));
        board.put(BoardCoordinate.H1, rook);

        assertThat(board.hasKingSideCastlingRight(PieceColor.WHITE), is(true));
    }


    @Test
    public void shouldLooseCastlingRightIfRookMoves() throws Exception {

        Piece king = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KING);
        Piece rook = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.ROOK);

        board.put(BoardCoordinate.E1, king);
        board.put(BoardCoordinate.H1, rook);

        board.movePiece(BoardCoordinate.H1, BoardCoordinate.H2);
        board.completePlay();

        boolean actual = board.hasKingSideCastlingRight(PieceColor.WHITE);

        assertThat(actual, is(false));

    }


    @Test
    public void shouldLooseCastlingRightIfKingMoves() throws Exception {

        Piece king = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KING);
        Piece rook = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.ROOK);

        board.put(BoardCoordinate.E1, king);
        board.put(BoardCoordinate.H1, rook);

        board.movePiece(BoardCoordinate.E1, BoardCoordinate.E2);
        board.completePlay();

        boolean actual = board.hasKingSideCastlingRight(PieceColor.WHITE);

        assertThat(actual, is(false));

    }


    @Test
    public void shouldRegainCaslingRightAfterUndo() throws Exception {

        Piece king = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KING);
        Piece rook = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.ROOK);

        board.put(BoardCoordinate.E1, king);
        board.put(BoardCoordinate.H1, rook);

        board.movePiece(BoardCoordinate.E1, BoardCoordinate.E2);
        board.completePlay();

        board.movePiece(BoardCoordinate.E2, BoardCoordinate.E1);
        board.completeUndo();

        boolean actual = board.hasKingSideCastlingRight(PieceColor.WHITE);

        assertThat(actual, is(true));

    }

    @Test
    public void getKingLocation() throws Exception {


        Piece blackKing = PieceMocks.newPieceMock(PieceColor.BLACK, PieceType.KING);
        Piece blackPawn = PieceMocks.newPieceMock(PieceColor.BLACK, PieceType.PAWN);

        Piece whiteKing = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KING);

        BoardCoordinate kingLocation = BoardCoordinate.E8;
        board.put(kingLocation, blackKing);
        board.put(BoardCoordinate.E7, blackPawn);

        board.put(BoardCoordinate.E1, whiteKing);

        assertThat(board.getKingLocation(PieceColor.BLACK), is(kingLocation));


    }
}