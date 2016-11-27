package com.erikmafo.javachess.pieces;


public class Piece {

    private final PieceColor color;
    private final PieceType type;

    public Piece(PieceColor color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    public PieceColor getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Piece{" +
                "color=" + color +
                ", type=" + type +
                '}';
    }

    public boolean is(PieceColor color, PieceType type) {
        return color.equals(this.color) && type.equals(this.type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;

        Piece piece = (Piece) o;

        if (color != piece.color) return false;
        return type == piece.type;

    }

    @Override
    public int hashCode() {
        int result = color.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}






