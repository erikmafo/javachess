package com.erikmafo.chess.move;

import com.erikmafo.chess.board.MoveReceiver;
import com.erikmafo.chess.board.Square;

/**
 * Created by erikmafo on 18.11.16.
 */
public abstract class AbstractMove implements Move {



    public static class Ranks {

        public static final int QUIET = 1;
        public static final int DOUBLE_PAWN_PUSH = 2;
        public static final int CASTLING = 3;
        public static final int CAPTURE = 4;
        public static final int EN_PASSENT = 5;
        public static final int PROMOTION = 6;
    }


    private final int rank;
    protected final MoveReceiver moveReceiver;
    protected final Square from;
    protected final Square to;



    protected AbstractMove(MoveReceiver moveReceiver, Square from, Square to, int rank) {
        this.rank = rank;
        this.moveReceiver = moveReceiver;
        this.from = from;
        this.to = to;
    }

    @Override
    public Square getFrom() {
        return from;
    }

    @Override
    public Square getTo() {
        return to;
    }



    protected abstract void beforePlayComplete();

    protected abstract void beforeUndoComplete();


    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void play() {
        moveReceiver.movePiece(from, to);
        beforePlayComplete();
        moveReceiver.completePlay();
    }

    @Override
    public void undo() {
        moveReceiver.movePiece(to, from);
        beforeUndoComplete();
        moveReceiver.completeUndo();
    }


    @Override
    public String toString() {
        return from + "-" + to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractMove that = (AbstractMove) o;

        if (from != that.from) return false;
        return to == that.to;
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}
