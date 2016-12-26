package com.erikmafo.chess.piece;


public enum PieceType {
    PAWN(false, 1), BISHOP(true, 3), KNIGHT(false, 3), ROOK(true, 5), QUEEN(true, 9), KING(false, 100);

    private boolean slides;
    private int value;

    PieceType(boolean slides, int value) {
        this.slides = slides;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean slides() {
        return slides;
    }


}
