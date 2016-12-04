package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.pieces.Piece;

/**
 * Created by erikmafo on 20.11.16.
 */
public class PawnPromotionMove extends AbstractMove {

    private final Piece pawn;
    private final Piece promoteTo;

    protected PawnPromotionMove(MoveReceiver moveReceiver, BoardCoordinate from, BoardCoordinate to, Piece pawn, Piece promoteTo) {
        super(moveReceiver, from, to, Ranks.PROMOTION);
        this.pawn = pawn;
        this.promoteTo = promoteTo;
    }

    @Override
    protected void beforePlayComplete() {
        moveReceiver.put(to, promoteTo);
    }

    @Override
    protected void beforeUndoComplete() {
        moveReceiver.put(from, pawn);
    }

}
