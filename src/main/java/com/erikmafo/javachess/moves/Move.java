package com.erikmafo.javachess.moves;


import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.MoveTarget;
import com.erikmafo.javachess.boardrep.Offset;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;


public abstract class Move {

    protected static final int QUIET_MOVE_SCORE = 1;
    protected static final int CASTLING_MOVE_SCORE = 2;
    protected static final int CAPTURE_MOVE_SCORE = 3;
    protected static final int EN_PASSENT_MOVE_SCORE = 4;
    protected static final int PAWN_PROMOTION_MOVE_SCORE = 5;

    protected final BoardCoordinate from, to;

    protected Move(BoardCoordinate from, BoardCoordinate to) {
        this.from = from;
        this.to = to;
    }

    public void execute(MoveTarget moveTarget) {
        moveTarget.movePiece(from, to);
    }

    public void rewind(MoveTarget moveTarget) {
        moveTarget.movePieceBackwards(to, from);
    }

    public BoardCoordinate getFrom() {
        return from;
    }

    public BoardCoordinate getTo() {
        return to;
    }

    public int getScore() {
        return QUIET_MOVE_SCORE;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Move) {
            Move otherMove = (Move) other;
            return otherMove.getFrom() == from && otherMove.getTo() == to;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }

}


class CastlingMove extends Move {

    private final BoardCoordinate rookFrom;
    private final BoardCoordinate rookTo;
    private final PieceColor color;

    protected CastlingMove(BoardCoordinate kingFrom, BoardCoordinate kingTo,
                           BoardCoordinate rookFrom, BoardCoordinate rookTo) {
        super(kingFrom, kingTo);
        this.rookFrom = rookFrom;
        this.rookTo = rookTo;
        this.color = kingFrom == BoardCoordinate.E1 ? PieceColor.WHITE : PieceColor.BLACK;

    }

    @Override
    public void execute(MoveTarget moveTarget) {
        moveTarget.movePiece(from, to);
        moveTarget.movePiece(rookFrom, rookTo);
        moveTarget.setHasCastled(color, true);
    }

    @Override
    public void rewind(MoveTarget moveTarget) {
        moveTarget.movePieceBackwards(to, from);
        moveTarget.movePieceBackwards(rookTo, rookFrom);
        moveTarget.setHasCastled(color, false);
    }

    @Override
    public int getScore() {
        return CASTLING_MOVE_SCORE;
    }
}

class QuietMove extends Move {
    public QuietMove(BoardCoordinate from, BoardCoordinate to) {
        super(from, to);
    }

    @Override
    public int getScore() {
        return QUIET_MOVE_SCORE;
    }
}


class CaptureMove extends Move {
    public CaptureMove(BoardCoordinate from, BoardCoordinate to) {
        super(from, to);
    }

    @Override
    public void execute(MoveTarget moveTarget) {
        moveTarget.movePieceAndDoCapture(from, to, to);
    }

    @Override
    public void rewind(MoveTarget moveTarget) {
        super.rewind(moveTarget);
        moveTarget.undoLastCapture();
    }

    @Override
    public int getScore() {
        return CAPTURE_MOVE_SCORE;
    }
}


class EnPassentMove extends Move {


    public EnPassentMove(BoardCoordinate from, BoardCoordinate to) {
        super(from, to);
    }

    @Override
    public void execute(MoveTarget moveTarget) {
        Offset offset = to.getRank() - from.getRank() > 0 ? Offset.DOWN : Offset.UP;
        moveTarget.movePieceAndDoCapture(from, to, to.getNext(offset));
    }

    @Override
    public void rewind(MoveTarget moveTarget) {
        super.rewind(moveTarget);
        moveTarget.undoLastCapture();
    }

    @Override
    public int getScore() {
        return EN_PASSENT_MOVE_SCORE;
    }


}

class PawnPromotionMove extends Move {
    private PieceType newPieceType;

    public PawnPromotionMove(BoardCoordinate from, BoardCoordinate to, PieceType newPieceType) {
        super(from, to);
        this.newPieceType = newPieceType;
    }

    @Override
    public void execute(MoveTarget moveTarget) {
        super.execute(moveTarget);
        moveTarget.promotePawn(to, newPieceType);
    }


    @Override
    public void rewind(MoveTarget moveTarget) {
        super.rewind(moveTarget);
        moveTarget.promotePawn(from, PieceType.PAWN);
    }

    @Override
    public int getScore() {
        return PAWN_PROMOTION_MOVE_SCORE;
    }
}








