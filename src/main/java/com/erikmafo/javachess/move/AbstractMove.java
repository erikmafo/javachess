package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.MoveReceiver;
import com.erikmafo.javachess.board.BoardCoordinate;

/**
 * Created by erikmafo on 18.11.16.
 */
public abstract class AbstractMove implements Move {

    protected final MoveReceiver moveReceiver;
    protected final BoardCoordinate from;
    protected final BoardCoordinate to;


    protected AbstractMove(MoveReceiver moveReceiver, BoardCoordinate from, BoardCoordinate to) {
        this.moveReceiver = moveReceiver;
        this.from = from;
        this.to = to;
    }

    @Override
    public BoardCoordinate getFrom() {
        return from;
    }

    @Override
    public BoardCoordinate getTo() {
        return to;
    }



    protected abstract void beforePlayComplete();

    protected abstract void beforeUndoComplete();




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
        if (!(o instanceof AbstractMove)) return false;

        AbstractMove that = (AbstractMove) o;

        if (!moveReceiver.equals(that.moveReceiver)) return false;
        if (from != that.from) return false;
        return to == that.to;

    }

    @Override
    public int hashCode() {
        int result = moveReceiver.hashCode();
        result = 31 * result + from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}
