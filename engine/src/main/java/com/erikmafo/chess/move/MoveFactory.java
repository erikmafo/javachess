package com.erikmafo.chess.move;

import com.erikmafo.chess.board.MoveReceiver;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.utils.DoubleEnumKeyMap;

/**
 * Created by erikmafo on 19.11.16.
 */
public class MoveFactory {

    private final DoubleEnumKeyMap<Square, Square, Move> quietMoves = new DoubleEnumKeyMap<>(Square.class, Square.class);

    public Move newSinglePawnPushMove(MoveReceiver moveReceiver, Square from, Square to) {

        return new QuietMove(moveReceiver, from, to);
    }


    public Move newQuietMove(MoveReceiver moveReceiver, Square from, Square to) {

        Move move;

        if (quietMoves.containsKeyCombination(from, to)) {
            move = quietMoves.get(from, to);
        } else {
            move = new QuietMove(moveReceiver, from, to);
            quietMoves.put(from, to, move);
        }

        return move;
    }

    public Move newCaptureMove(MoveReceiver moveReceiver, Square from, Square to, Piece capturedPiece) {
        return new CaptureMove(moveReceiver, from, to, capturedPiece);
    }

    public Move newEnPassentMove(MoveReceiver moveReceiver, Square from, Square to, Piece capturedPiece) {
        return new EnPassentMove(moveReceiver, from, to, capturedPiece);
    }

    public Move newCastlingMove(MoveReceiver moveReceiver, Square kingFrom, Square kingTo, Square rookFrom, Square rookTo) {
        return new CastlingMove(moveReceiver, kingFrom, kingTo, rookFrom, rookTo);
    }

    public Move newDoublePawnPushMove(MoveReceiver moveReceiver, Square from, Square to) {
        return new DoublePawnPushMove(moveReceiver, from, to);
    }

    public Move newPawnPromotionMove(MoveReceiver moveReceiver, Square from, Square to, Piece pawn, Piece promoteTo) {
        return new PawnPromotionMove(moveReceiver, from, to, pawn, promoteTo);
    }



}

