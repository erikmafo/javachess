package com.erikmafo.javachess.boardrep;

import com.erikmafo.javachess.moves.Move;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;


public interface ReadableBoard {

    boolean isOccupiedAt(BoardCoordinate location);

    PieceColor getPieceColorAt(BoardCoordinate location);

    PieceType getPieceTypeAt(BoardCoordinate location);

    PieceColor getMovingColor();

    BoardCoordinate getKingLocation(PieceColor color);

    Move getLastMove();

    int getAttackedSquaresCount(PieceColor color);

    int sumMaterial(PieceColor color);

    boolean isAttacked(PieceColor color, BoardCoordinate coordinate);

    boolean isChecked(PieceColor color);

    boolean isKingSideCastlingPossible(PieceColor color);

    boolean isQueenSideCastlingPossible(PieceColor color);

    boolean hasCastled(PieceColor color);

}
