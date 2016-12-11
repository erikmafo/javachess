package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BasicOffset;
import com.erikmafo.javachess.board.Square;
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

        when(board.pieceAt(any(Square.class))).thenReturn(Optional.ofNullable(null));

    }


    public Object[] slideSearchFixtures() {

        return new Object[] {
                new Object[]{Optional.ofNullable(whitePawn), whitePawn, Square.G8, PieceColor.WHITE, Square.G6, BasicOffset.UP},
                new Object[]{Optional.empty(), whitePawn, Square.G8, PieceColor.BLACK, Square.G6, BasicOffset.UP},
                new Object[]{Optional.empty(), null, Square.OFF_BOARD, PieceColor.WHITE, Square.G6, BasicOffset.UP}


        };
    }



    @Test
    @Parameters(method = "slideSearchFixtures")
    public void slideSearch(Optional<Piece> expected,
                            Piece piece,
                            Square square,
                            PieceColor colorToFind,
                            Square start,
                            Offset offset) throws Exception {

        when(board.pieceAt(square)).thenReturn(Optional.ofNullable(piece));

        Predicate<Piece> predicate = p -> colorToFind.equals(p.getColor());

        Optional<Piece> pieceOptional = boardSeeker.search(predicate, board, start, true, offset);

        assertThat(pieceOptional, is(expected));
    }


    @Test
    public void lookOutForBlockingPiece() throws Exception {

        PieceColor opponent = PieceColor.BLACK;

        Piece blockingPiece = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.PAWN);
        Piece targetPiece = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KING);
        Piece slidingPiece = PieceMocks.newPieceMock(PieceColor.BLACK, PieceType.ROOK);

        Square blockingPieceSquare = Square.G5;
        Square targetPieceSquare = Square.G4;
        Square slidingPieceSquare = Square.G8;

        when(board.pieceAt(blockingPieceSquare)).thenReturn(Optional.ofNullable(blockingPiece));
        when(board.pieceAt(targetPieceSquare)).thenReturn(Optional.ofNullable(targetPiece));
        when(board.pieceAt(slidingPieceSquare)).thenReturn(Optional.ofNullable(slidingPiece));

        boolean isAttaked = boardSeeker.isAttackedBy(opponent, targetPieceSquare, board);

        assertThat("" + targetPiece + " is not attacked because " + blockingPiece + " is blocking the " + slidingPiece,
                isAttaked, is(false));
    }



    @Test
    public void getMobilityCount() throws Exception {

        MobilityCount mobilityCount = boardSeeker.getMobilityCount(board, Square.H1, true, BasicOffset.UP);

        assertThat(mobilityCount.getEmptySquares(), is(7));
        assertThat(mobilityCount.getGetOccupiedByBlack(), is(0));
        assertThat(mobilityCount.getOccupiedByWhite(), is(0));
    }





}