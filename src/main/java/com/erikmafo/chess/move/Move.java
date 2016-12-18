package com.erikmafo.chess.move;

import com.erikmafo.chess.board.Square;

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
