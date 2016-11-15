package com.erikmafo.javachess.moves;

import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.ReadableBoard;
import com.erikmafo.javachess.pieces.AttackTable;
import com.erikmafo.javachess.pieces.PieceColor;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by erikmafo on 15.11.16.
 */
public class KingMoveGeneratorTest {


    private PieceColor pieceColor = PieceColor.WHITE;
    private BoardCoordinate from = BoardCoordinate.E1;

    private KingMoveGenerator kingMoveGenerator = new KingMoveGenerator(pieceColor);


    private ReadableBoard board = mock(ReadableBoard.class);

    private AttackTable attackTable = mock(AttackTable.class);
    private List<Move> moves = new ArrayList<>();


    @Test
    public void findKingSideCastlingMoveWhenLegal() throws Exception {

        BoardCoordinate to = BoardCoordinate.G1;

        when(board.isKingSideCastlingPossible(pieceColor)).thenReturn(true);

        kingMoveGenerator.findPossibleMoves(board, from, attackTable, moves);

        assertThat(moves, CoreMatchers.hasItem(IsMove.isMove(CastlingMove.class, from, to)));
    }


    @Test
    public void findQueenSideCastlingMoveWhenLegal() throws Exception {

        BoardCoordinate to = BoardCoordinate.C1;

        when(board.isQueenSideCastlingPossible(pieceColor)).thenReturn(true);

        kingMoveGenerator.findPossibleMoves(board, from, attackTable, moves);

        assertThat(moves, CoreMatchers.hasItem(IsMove.isMove(CastlingMove.class, from, to)));
    }



}