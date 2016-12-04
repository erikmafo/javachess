package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.Square;

/**
 * Created by erikmafo on 18.11.16.
 */
public interface Move {

    Square getFrom();

    Square getTo();

    void play();

    void undo();

    int getRank();

}
