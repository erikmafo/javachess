package com.erikmafo.chess.board;

import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import com.erikmafo.chess.testingutils.PieceMocks;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikf on 12/10/2016.
 */
public class ZobristCalculatorTest {


    private long longValue = 0b1010000101000101101000010100010110100001010001011010000101000101L;;
    private long zero = 0L;
    private Random random = mock(Random.class);

    private ZobristCalculator zobristCalculator;

    @Before
    public void setUp() throws Exception {

         when(random.nextLong()).thenReturn(longValue);

         zobristCalculator = new ZobristCalculator(random);

    }

    @Test
    public void testShiftColorToMove() throws Exception {

        zobristCalculator.shiftColorToMove();

        assertThat(zobristCalculator.getValue(), is(longValue));

        zobristCalculator.shiftColorToMove();

        assertThat(zobristCalculator.getValue(), is(zero));
    }


    @Test
    public void testShiftPiece() throws Exception {

        Piece piece = PieceMocks.newPieceMock(PieceColor.BLACK, PieceType.BISHOP);
        Square square = Square.A1;

        zobristCalculator.shiftPiece(square, piece);

        assertThat(zobristCalculator.getValue(), is(longValue));

        zobristCalculator.shiftPiece(square, piece);

        assertThat(zobristCalculator.getValue(), is(zero));

    }

    @Test
    public void testShiftCastlingRight() throws Exception {

        PieceColor color = PieceColor.BLACK;
        CastlingRight castlingRight = CastlingRight.BOTH;

        zobristCalculator.shiftCastlingRight(color, castlingRight);

        assertThat(zobristCalculator.getValue(), is(longValue));

        zobristCalculator.shiftCastlingRight(color, castlingRight);

        assertThat(zobristCalculator.getValue(), is(zero));
    }
}