package com.erikmafo.javachess.board;

import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;
import com.erikmafo.javachess.testingutils.PieceMocks;
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
public class ZobristTableTest {


    private long longValue = 0b1010000101000101101000010100010110100001010001011010000101000101L;;
    private long zero = 0L;
    private Random random = mock(Random.class);

    private ZobristTable zobristTable;

    @Before
    public void setUp() throws Exception {

         when(random.nextLong()).thenReturn(longValue);

         zobristTable = new ZobristTable(random);

    }

    @Test
    public void testShiftColorToMove() throws Exception {

        zobristTable.shiftColorToMove();

        assertThat(zobristTable.getValue(), is(longValue));

        zobristTable.shiftColorToMove();

        assertThat(zobristTable.getValue(), is(zero));
    }


    @Test
    public void testShiftPiece() throws Exception {

        Piece piece = PieceMocks.newPieceMock(PieceColor.BLACK, PieceType.BISHOP);
        Square square = Square.A1;

        zobristTable.shiftPiece(square, piece);

        assertThat(zobristTable.getValue(), is(longValue));

        zobristTable.shiftPiece(square, piece);

        assertThat(zobristTable.getValue(), is(zero));

    }

    @Test
    public void testShiftCastlingRight() throws Exception {

        PieceColor color = PieceColor.BLACK;
        CastlingRight castlingRight = CastlingRight.BOTH;

        zobristTable.shiftCastlingRight(color, castlingRight);

        assertThat(zobristTable.getValue(), is(longValue));

        zobristTable.shiftCastlingRight(color, castlingRight);

        assertThat(zobristTable.getValue(), is(zero));
    }
}