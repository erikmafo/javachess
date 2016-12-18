package com.erikmafo.chess.move;

import com.erikmafo.chess.board.MoveReceiver;
import com.erikmafo.chess.board.BasicOffset;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.board.Offset;
import com.erikmafo.chess.piece.Piece;

/**
 * Created by erikmafo on 18.11.16.
 */
public class EnPassentMove extends AbstractMove {

    private final Piece capturedPiece;
    private final Square capturePieceCoordinate;

    public EnPassentMove(MoveReceiver moveReceiver, Square from, Square to, Piece capturedPiece) {
        super(moveReceiver, from, to, Ranks.EN_PASSENT);
        this.capturedPiece = capturedPiece;
        Offset offset = to.getRank() - from.getRank() > 0 ? BasicOffset.DOWN : BasicOffset.UP;
        capturePieceCoordinate = to.next(offset);
    }

    @Override
    protected void beforePlayComplete() {
        moveReceiver.remove(capturePieceCoordinate);
        moveReceiver.resetHalfMoveClock();
    }

    @Override
    protected void beforeUndoComplete() {
        moveReceiver.put(capturePieceCoordinate, capturedPiece);
    }

}
