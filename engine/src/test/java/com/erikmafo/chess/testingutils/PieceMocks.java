package com.erikmafo.chess.testingutils;

import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erikmafo on 26.11.16.
 */
public class PieceMocks {


    public static Piece newPieceMock(PieceColor color, PieceType type) {

        String name = "" + color + " " + type;

        Piece mock = mock(Piece.class, name.toLowerCase());

        when(mock.getColor()).thenReturn(color);
        when(mock.getType()).thenReturn(type);
        when(mock.is(color, type)).thenReturn(true);

        return mock;

    }

}
