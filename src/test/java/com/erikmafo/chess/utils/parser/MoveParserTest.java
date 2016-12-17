package com.erikmafo.chess.utils.parser;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.move.MoveFactory;
import com.erikmafo.chess.pieces.Piece;
import com.erikmafo.chess.pieces.PieceColor;
import com.erikmafo.chess.pieces.PieceType;
import com.erikmafo.chess.testingutils.PieceMocks;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 11.12.16.
 */
@RunWith(JUnitParamsRunner.class)
public class MoveParserTest {

    private Board board = mock(Board.class);
    private MoveFactory moveFactory = mock(MoveFactory.class);

    private MoveParser moveParser = new MoveParser(moveFactory);


    private String getLongAlgebraicMoveString(Square from, Square to) {
        return ("" + from + to).toLowerCase();
    }


    @Parameters({"", "e2-e4", "e2de4", "e5", "h1i1", "h0h1"})
    @Test(expected = MoveFormatException.class)
    public void throwMoveFormatExceptionIfInvalidMoveString(String move) throws Exception {

        moveParser.parse(board, move, MoveParser.Format.LONG_ALGEBRAIC);

    }

    @Test
    public void parseLongAlgebraicEnPassentMove() throws Exception {

        MoveParser.Format format = MoveParser.Format.LONG_ALGEBRAIC;
        Square from = Square.E5;
        Square to = Square.F6;
        Square enPassentTarget = Square.F5;

        Piece piece = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.PAWN);
        Piece captured = PieceMocks.newPieceMock(PieceColor.BLACK, PieceType.PAWN);
        String moveString = getLongAlgebraicMoveString(from, to);
        when(board.enPassentTarget()).thenReturn(Optional.ofNullable(enPassentTarget));
        when(board.getNullablePiece(enPassentTarget)).thenReturn(captured);
        when(board.getNullablePiece(from)).thenReturn(piece);

        Move expected = mock(Move.class);
        when(moveFactory.newEnPassentMove(board, from, to, captured)).thenReturn(expected);

        Move move = moveParser.parse(board, moveString, format);

        assertThat(move, is(expected));

    }


    @Test
    public void parseLongAlgebraicCaptureMove() throws Exception {

        MoveParser.Format format = MoveParser.Format.LONG_ALGEBRAIC;
        Square from = Square.G1;
        Square to = Square.F3;
        Piece piece = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KNIGHT);
        Piece captured = PieceMocks.newPieceMock(PieceColor.BLACK, PieceType.PAWN);
        String moveString = getLongAlgebraicMoveString(from, to);
        when(board.isOccupied(to)).thenReturn(true);
        when(board.getNullablePiece(to)).thenReturn(captured);
        when(board.getNullablePiece(from)).thenReturn(piece);
        Move expected = mock(Move.class);
        when(moveFactory.newCaptureMove(board, from, to, captured)).thenReturn(expected);

        Move move = moveParser.parse(board, moveString, format);

        assertThat(move, is(expected));

    }

    @Test
    public void parseLongAlgebraicQuietMove() throws Exception {

        MoveParser.Format format = MoveParser.Format.LONG_ALGEBRAIC;
        Square from = Square.G1;
        Square to = Square.F3;
        Piece piece = PieceMocks.newPieceMock(PieceColor.WHITE, PieceType.KNIGHT);
        String moveString = getLongAlgebraicMoveString(from, to);
        when(board.getNullablePiece(from)).thenReturn(piece);
        Move expected = mock(Move.class);
        when(moveFactory.newQuietMove(board, from, to)).thenReturn(expected);

        Move move = moveParser.parse(board, moveString, format);

        assertThat(move, is(expected));
    }








}