package com.erikmafo.chess.move;

import com.erikmafo.chess.board.MoveReceiver;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;

/**
 * Created by erikmafo on 18.11.16.
 */
public class CaptureMove extends AbstractMove {

    private final Piece capturedPiece;

    public CaptureMove(MoveReceiver moveReceiver, Square from, Square to, Piece capturedPiece) {
        super(moveReceiver, from, to, Ranks.CAPTURE + getValue(capturedPiece));
        this.capturedPiece = capturedPiece;
    }

    private static int getValue(Piece piece) {

        if (piece.getType() == null) {
            return 0;
        }

        int val;
        switch (piece.getType()) {
            case PAWN:
                val = 0;
                break;
            case BISHOP:
            case KNIGHT:
                val = 1;
                break;
            case ROOK:
                val = 2;
                break;
            case QUEEN:
                val = 3;
                break;
            case KING:
                val = 4;
                break;
            default:
                throw new AssertionError();
        }
        return val;
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
