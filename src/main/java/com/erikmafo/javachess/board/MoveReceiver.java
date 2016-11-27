package com.erikmafo.javachess.board;

import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;

/**
 * Created by erikmafo on 18.11.16.
 */
public interface MoveReceiver {

    void movePiece(BoardCoordinate from, BoardCoordinate to);

    void remove(BoardCoordinate boardCoordinate);

    void put(BoardCoordinate boardCoordinate, Piece piece);

    void setEnPassentTarget(BoardCoordinate boardCoordinate);

    void removeEnPassentTarget();

    void setHasCastled(PieceColor color, boolean hasCastled);

    void resetHalfMoveClock();

    void completePlay();

    void completeUndo();

}
