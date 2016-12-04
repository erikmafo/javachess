package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.pieces.PieceColor;

/**
 * Created by erikmafo on 19.11.16.
 */
public class CastlingMove extends AbstractMove {

    private final Square rookFrom;
    private final Square rookTo;

    private final PieceColor color;

    protected CastlingMove(MoveReceiver moveReceiver, Square from, Square to, Square rookFrom, Square rookTo) {
        super(moveReceiver, from, to, Ranks.CASTLING);
        this.rookFrom = rookFrom;
        this.rookTo = rookTo;
        color = rookFrom.getRank() == 0 ? PieceColor.WHITE : PieceColor.BLACK;
    }

    @Override
    protected void beforePlayComplete() {

        moveReceiver.movePiece(rookFrom, rookTo);
        moveReceiver.setHasCastled(color, true);

    }

    @Override
    protected void beforeUndoComplete() {

        moveReceiver.movePiece(rookTo, rookFrom);
        moveReceiver.setHasCastled(color, false);

    }

}
