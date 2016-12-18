package com.erikmafo.chess.piece;


public enum PieceColor {
    BLACK, WHITE;

    public PieceColor getOpposite() {
        return this == WHITE ? BLACK : WHITE;
    }


    public boolean isWhite() {
        return this == WHITE;
    }
}
