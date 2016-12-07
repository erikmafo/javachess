package com.erikmafo.javachess.board;

import com.erikmafo.javachess.pieces.PieceColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public enum BasicOffset implements Offset {

    UP(16),
    UP_RIGHT(17),
    RIGHT(1),
    DOWN_RIGHT(-15),
    DOWN(-16),
    DOWN_LEFT(-17),
    LEFT(-1),
    UP_LEFT(15);


    public static Offset[] rookValues() {
        return new Offset[] {UP, RIGHT, DOWN, LEFT};
    }

    public static Offset[] bishopValues() {
        return new Offset[] {UP_RIGHT, DOWN_RIGHT, DOWN_LEFT, UP_LEFT};
    }

    private final int value;

    BasicOffset(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }


    public static Offset valueOf(Square from, Square to) {
        return directionBy0x88Diff[diff(from, to)];
    }

    private static int diff(Square sq1, Square sq2){
        return  120 + sq2.getX88Value() - sq1.getX88Value();
    }

    private static final Offset[] directionBy0x88Diff;

    static {
        directionBy0x88Diff = new BasicOffset[240];
        for (Square sq : Square.values()) {
            for (Offset offset : BasicOffset.values()) {
                Square current = sq;
                while (current.hasNext(offset)) {
                    current = current.next(offset);
                    directionBy0x88Diff[diff(sq, current)] = offset;
                }
            }
        }
    }



}
