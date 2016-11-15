package com.erikmafo.javachess.pieces;


public enum PieceType {
    PAWN(1), BISHOP(3), KNIGHT(3), ROOK(5), QUEEN(9), KING(100);

    private int value;

    PieceType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
