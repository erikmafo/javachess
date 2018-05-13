package com.erikmafo.chess.piece;


public enum PieceColor {
    BLACK, WHITE;

    public PieceColor opponent() {
        return this == WHITE ? BLACK : WHITE;
    }


    public boolean isWhite() {
        return this == WHITE;
    }
}
