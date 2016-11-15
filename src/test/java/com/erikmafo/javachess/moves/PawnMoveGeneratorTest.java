package com.erikmafo.javachess.moves;

import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.ReadableBoard;
import com.erikmafo.javachess.pieces.AttackTable;
import com.erikmafo.javachess.pieces.PieceColor;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.erikmafo.javachess.moves.IsMove.isMove;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


/**
 * Created by erikmafo on 15.11.16.
 */
public class PawnMoveGeneratorTest {

    private PieceColor pieceColor = PieceColor.BLACK;
    private PawnMoveGenerator pawnMoveGenerator = new PawnMoveGenerator(pieceColor);


    private ReadableBoard board = mock(ReadableBoard.class);


    private AttackTable attackTable = mock(AttackTable.class);
    private List<Move> moves = new ArrayList<>();

    @Test
    public void findDoublePawnPush() throws Exception {

        BoardCoordinate from = BoardCoordinate.A7;
        BoardCoordinate to = BoardCoordinate.A5;

        pawnMoveGenerator.findPossibleMoves(board, from, attackTable, moves);

        assertThat(moves, CoreMatchers.hasItem(isMove(from, to)));
    }



}