package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.pieces.Piece;

/**
 * Created by erikmafo on 18.11.16.
 */
public class CaptureMove extends AbstractMove {

    private final Piece capturedPiece;

    public CaptureMove(MoveReceiver moveReceiver, Square from, Square to, Piece capturedPiece) {
        super(moveReceiver, from, to, Ranks.CAPTURE);
        this.capturedPiece = capturedPiece;
    }

    @Override
    protected void beforePlayComplete() {
        moveReceiver.resetHalfMoveClock();
    }

    @Override
    protected void beforeUndoComplete() {
        moveReceiver.put(to, capturedPiece);
    }

}
