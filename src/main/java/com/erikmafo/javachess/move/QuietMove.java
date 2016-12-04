package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.Square;

/**
 * Created by erikmafo on 18.11.16.
 */
public class QuietMove extends AbstractMove {

    public QuietMove(MoveReceiver moveReceiver, Square from, Square to) {
        super(moveReceiver, from, to, Ranks.QUIET);
    }

    @Override
    protected void beforePlayComplete() {

    }

    @Override
    protected void beforeUndoComplete() {

    }

}
