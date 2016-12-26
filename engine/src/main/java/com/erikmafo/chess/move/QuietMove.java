package com.erikmafo.chess.move;

import com.erikmafo.chess.board.MoveReceiver;
import com.erikmafo.chess.board.Square;

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
