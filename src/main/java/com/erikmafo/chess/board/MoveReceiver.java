package com.erikmafo.chess.board;

import com.erikmafo.chess.pieces.Piece;
import com.erikmafo.chess.pieces.PieceColor;

/**
 * Created by erikmafo on 18.11.16.
 */
public interface MoveReceiver {

    void movePiece(Square from, Square to);

    Piece remove(Square square);

    Piece put(Square square, Piece piece);

    void setEnPassentTarget(Square square);

    void removeEnPassentTarget();

    void setHasCastled(PieceColor color, boolean hasCastled);

    void resetHalfMoveClock();

    void completePlay();

    void completeUndo();

}
