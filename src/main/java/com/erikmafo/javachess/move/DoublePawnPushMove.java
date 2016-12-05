package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.BasicOffset;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.board.Offset;

/**
 * Created by erikmafo on 18.11.16.
 */
public class DoublePawnPushMove extends AbstractMove {

    private final Square enPassentTarget;

    protected DoublePawnPushMove(MoveReceiver moveReceiver, Square from, Square to) {
        super(moveReceiver, from, to, Ranks.DOUBLE_PAWN_PUSH);
        Offset dir = from.getRank() > to.getRank() ? BasicOffset.UP : BasicOffset.DOWN;
        enPassentTarget = to.next(dir);
    }

    @Override
    protected void beforePlayComplete() {
        moveReceiver.setEnPassentTarget(enPassentTarget);
        moveReceiver.resetHalfMoveClock();
    }

    @Override
    protected void beforeUndoComplete() {
        moveReceiver.removeEnPassentTarget();
        moveReceiver.resetHalfMoveClock();
    }

}
