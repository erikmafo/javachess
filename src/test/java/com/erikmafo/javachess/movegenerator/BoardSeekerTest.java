package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.board.Offset;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;
import com.erikmafo.javachess.testingutils.PieceMocks;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;
import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 20.11.16.
 */
@RunWith(JUnitParamsRunner.class)
public class BoardSeekerTest {

    private BoardSeeker boardSeeker = new BoardSeeker();

    private Board board = mock(Board.class, "Board");

    private Piece whitePawn = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.PAWN);


    @Before
    public void setUp() throws Exception {

        when(board.pieceAt(any(BoardCoordinate.class))).thenReturn(Optional.ofNullable(null));

    }


    public Object[] fixtures() {

        return new Object[] {
                new Object[]{Optional.ofNullable(whitePawn), whitePawn, BoardCoordinate.G8, PieceColor.WHITE, BoardCoordinate.G6, Offset.UP},
                new Object[]{Optional.empty(), whitePawn, BoardCoordinate.G8, PieceColor.BLACK, BoardCoordinate.G6, Offset.UP},
                new Object[]{Optional.empty(), null, BoardCoordinate.OFF_BOARD, PieceColor.WHITE, BoardCoordinate.G6, Offset.UP}


        };
    }



    @Test
    @Parameters(method = "knightMovesTestFixtures")
    public void slideSearch(Optional<Piece> expected,
                            Piece piece,
                            BoardCoordinate square,
                            PieceColor colorToFind,
                            BoardCoordinate start,
                            Offset offset) throws Exception {

        when(board.pieceAt(square)).thenReturn(Optional.ofNullable(piece));

        Predicate<Piece> predicate = p -> colorToFind.equals(p.getColor());

        Optional<Piece> pieceOptional = boardSeeker.search(predicate, board, start, true, offset);

        assertThat(pieceOptional, is(expected));
    }





}