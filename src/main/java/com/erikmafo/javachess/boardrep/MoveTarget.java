package com.erikmafo.javachess.boardrep;

import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;


public interface MoveTarget {

    /**
     * Should be called when a move is executed.
     */
    void movePiece(BoardCoordinate from, BoardCoordinate to);

    /**
     * Should be called when a capture move is executed.
     *
     * @param from
     * @param to
     * @param capturedPieceLocation
     */
    void movePieceAndDoCapture(BoardCoordinate from, BoardCoordinate to, BoardCoordinate capturedPieceLocation);


    /**
     * Should be called when a move is reversed.
     */
    void movePieceBackwards(BoardCoordinate from, BoardCoordinate to);


    /**
     * Should be called when a capture move is reversed
     */
    void undoLastCapture();

    /**
     * Should be called when a pawn is promoted
     *
     * @param boardCoordinate
     * @param newPieceType
     */
    void promotePawn(BoardCoordinate boardCoordinate, PieceType newPieceType);


    /**
     * Should be called when the castling rights of a color changes
     *
     * @param color
     * @param hasCastled
     */
    void setHasCastled(PieceColor color, boolean hasCastled);
}
