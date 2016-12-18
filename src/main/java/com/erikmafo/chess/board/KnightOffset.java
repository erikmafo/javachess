package com.erikmafo.chess.board;

/**
 * Created by erikmafo on 05.12.16.
 */
public enum KnightOffset implements Offset {

    KNIGHT_LEAP_2UP_LEFT(31),
    KNIGHT_LEAP_2UP_RIGHT(33),
    KNIGHT_LEAP_UP_2RIGHT(18),
    KNIGHT_LEAP_UP_2LEFT(14),
    KNIGHT_LEAP_2DOWN_RIGHT(-31),
    KNIGHT_LEAP_2DOWN_LEFT(-33),
    KNIGHT_LEAP_DOWN_2LEFT(-18),
    KNIGHT_LEAP_DOWN_2RIGHT(-14);

    private final int value;

    KnightOffset(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }


}
