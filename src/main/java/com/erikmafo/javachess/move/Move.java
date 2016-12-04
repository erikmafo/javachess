package com.erikmafo.javachess.move;

import com.erikmafo.javachess.board.BoardCoordinate;

/**
 * Created by erikmafo on 18.11.16.
 */
public interface Move {

    BoardCoordinate getFrom();

    BoardCoordinate getTo();

    void play();

    void undo();

    int getRank();

}
