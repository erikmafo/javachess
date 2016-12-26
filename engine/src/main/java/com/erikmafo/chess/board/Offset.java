package com.erikmafo.chess.board;

/**
 * Represents the difference between two chess squares.
 *
 * @author Erik Folstad
 */
public interface Offset {

    /**
     * Gets the 0x88 value interpretation of this <code>Offset</code>
     *
     * @return - an integer between 0 and 119
     */
    int getValue();
}
