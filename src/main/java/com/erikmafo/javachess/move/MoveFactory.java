package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.pieces.Piece;

/**
 * Created by erikmafo on 19.11.16.
 */
public class MoveFactory {

    private final MoveReceiver moveReceiver;


    public MoveFactory(MoveReceiver moveReceiver) {
        this.moveReceiver = moveReceiver;
    }


    public Move newSinglePawnPushMove(Square from, Square to) {
        return new QuietMove(moveReceiver, from, to);
    }


    public Move newQuietMove(Square from, Square to) {
        return new QuietMove(moveReceiver, from, to);
    }

    public Move newCaptureMove(Square from, Square to, Piece capturedPiece) {
        return new CaptureMove(moveReceiver, from, to, capturedPiece);
    }

    public Move newEnPassentMove(Square from, Square to, Piece capturedPiece) {
        return new EnPassentMove(moveReceiver, from, to, capturedPiece);
    }

    public Move newCastlingMove(Square kingFrom, Square kingTo, Square rookFrom, Square rookTo) {
        return new CastlingMove(moveReceiver, kingFrom, kingTo, rookFrom, rookTo);
    }

    public Move newDoublePawnPushMove(Square from, Square to) {
        return new DoublePawnPushMove(moveReceiver, from, to);
    }

    public Move newPawnPromotionMove(Square from, Square to, Piece pawn, Piece promoteTo) {
        return new PawnPromotionMove(moveReceiver, from, to, pawn, promoteTo);
    }



}
