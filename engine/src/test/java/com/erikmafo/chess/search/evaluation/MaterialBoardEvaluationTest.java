package com.erikmafo.chess.search.evaluation;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import com.erikmafo.chess.testingutils.PieceMocks;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    private Set<Square> occupiedSquares = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        when(board.pieceAt(any(Square.class))).thenReturn(Optional.empty());
        when(board.getColorToMove()).thenReturn(PieceColor.WHITE);
        when(board.getOccupiedSquares()).thenReturn(occupiedSquares);


    }

    private void putPiece(Square sq, Piece piece) {

        when(board.isOccupied(sq)).thenReturn(true);
        when(board.getNullablePiece(sq)).thenReturn(piece);
        when(board.pieceAt(sq)).thenReturn(Optional.ofNullable(piece));

        occupiedSquares.add(sq);
    }



    @Test
    public void testMaterialEvalualtion() throws Exception {

        Piece whiteBishop= PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.BISHOP);
        Piece whiteKing = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KING);

        Piece blackKing = PieceMocks.newPieceMock(PieceColor.BLACK, PieceType.KING);

        putPiece(Square.E1, whiteKing);
        putPiece(Square.F1, whiteBishop);

        putPiece(Square.E8, blackKing);

        assertThat(evaluation.applyAsInt(board), is(MaterialBoardEvaluation.DEFAULT_BISHOP_VALUE));

        when(board.getColorToMove()).thenReturn(PieceColor.BLACK);

        assertThat(evaluation.applyAsInt(board), is(-MaterialBoardEvaluation.DEFAULT_BISHOP_VALUE));
    }
}