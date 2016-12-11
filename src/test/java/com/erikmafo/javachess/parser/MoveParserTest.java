package com.erikmafo.javachess.parser;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.move.QuietMove;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;
import com.erikmafo.javachess.testingutils.PieceMocks;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 11.12.16.
 */
public class MoveParserTest {

    private Board board = mock(Board.class);
    private MoveFactory moveFactory = mock(MoveFactory.class);

    private MoveParser moveParser = new MoveParser(moveFactory);


    private String getLongAlgebraicMoveString(Square from, Square to) {
        return ("" + from + to).toLowerCase();
    }

    @Test
    public void parseLongAlgebraicQuietMove() throws Exception {

        MoveParser.Format format = MoveParser.Format.LONG_ALGEBRAIC;
        Square from = Square.G1;
        Square to = Square.F3;
        Piece piece = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KNIGHT);
        String moveString = getLongAlgebraicMoveString(from, to);
        when(board.isOccupied(to)).thenReturn(false);
        when(board.getNullablePiece(from)).thenReturn(piece);
        Move expected = mock(Move.class);
        when(moveFactory.newQuietMove(board, from, to)).thenReturn(expected);

        Move move = moveParser.parse(board, moveString, format);

        assertThat(move, is(expected));
    }

}