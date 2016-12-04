package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;
import com.erikmafo.javachess.testingutils.PieceMocks;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 27.11.16.
 */
public class MaterialBoardEvaluationTest {

    private MaterialBoardEvaluation evaluation = new MaterialBoardEvaluation();
    private Board board = mock(Board.class);

    @Before
    public void setUp() throws Exception {
        when(board.pieceAt(any(Square.class))).thenReturn(Optional.empty());
        when(board.getColorToMove()).thenReturn(PieceColor.WHITE);


    }

    private void putPiece(Square sq, Piece piece) {

        when(board.isOccupied(sq)).thenReturn(true);
        when(board.getNullablePiece(sq)).thenReturn(piece);
        when(board.pieceAt(sq)).thenReturn(Optional.ofNullable(piece));
    }



    @Test
    public void testMaterialEvalualtion() throws Exception {

        Piece whiteBishop= PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.BISHOP);
        Piece whiteKing = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KING);

        Piece blackKing = PieceMocks.newPieceMock(PieceColor.BLACK, PieceType.KING);

        putPiece(Square.E1, whiteKing);
        putPiece(Square.F1, whiteBishop);

        putPiece(Square.E8, blackKing);

        assertThat(evaluation.applyAsInt(board), is(3));

        when(board.getColorToMove()).thenReturn(PieceColor.BLACK);

        assertThat(evaluation.applyAsInt(board), is(-3));
    }
}