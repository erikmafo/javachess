package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.pieces.Piece;

/**
 * Created by erikmafo on 19.11.16.
 */
public class MoveFactory {

    private final MoveReceiver moveReceiver;


    public MoveFactory(MoveReceiver moveReceiver) {
        this.moveReceiver = moveReceiver;
    }


    public Move newSinglePawnPushMove(BoardCoordinate from, BoardCoordinate to) {
        return new QuietMove(moveReceiver, from, to);
    }


    public Move newQuietMove(BoardCoordinate from, BoardCoordinate to) {
        return new QuietMove(moveReceiver, from, to);
    }

    public Move newCaptureMove(BoardCoordinate from, BoardCoordinate to, Piece capturedPiece) {
        return new CaptureMove(moveReceiver, from, to, capturedPiece);
    }

    public Move newEnPassentMove(BoardCoordinate from, BoardCoordinate to, Piece capturedPiece) {
        return new EnPassentMove(moveReceiver, from, to, capturedPiece);
    }

    public Move newCastlingMove(BoardCoordinate kingFrom, BoardCoordinate kingTo, BoardCoordinate rookFrom, BoardCoordinate rookTo) {
        return new CastlingMove(moveReceiver, kingFrom, kingTo, rookFrom, rookTo);
    }

    public Move newDoublePawnPushMove(BoardCoordinate from, BoardCoordinate to) {
        return new DoublePawnPushMove(moveReceiver, from, to);
    }

    public Move newPawnPromotionMove(BoardCoordinate from, BoardCoordinate to, Piece pawn, Piece promoteTo) {
        return new PawnPromotionMove(moveReceiver, from, to, pawn, promoteTo);
    }


}
