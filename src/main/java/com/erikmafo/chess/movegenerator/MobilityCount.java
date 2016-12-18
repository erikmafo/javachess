package com.erikmafo.chess.movegenerator;

import com.erikmafo.chess.piece.PieceColor;

/**
 * Created by erikmafo on 11.12.16.
 */
public class MobilityCount {

    private final int emptySquares;
    private final int occupiedByWhite;
    private final int getOccupiedByBlack;

    public MobilityCount(int emptySquares, int occupiedByWhite, int getOccupiedByBlack) {
        this.emptySquares = emptySquares;
        this.occupiedByWhite = occupiedByWhite;
        this.getOccupiedByBlack = getOccupiedByBlack;
    }

    public int getEmptySquares() {
        return emptySquares;
    }

    public int getOccupiedByWhite() {
        return occupiedByWhite;
    }

    public int getGetOccupiedByBlack() {
        return getOccupiedByBlack;
    }


    public int getOccupiedBy(PieceColor color) {
        return color.isWhite() ? occupiedByWhite : getOccupiedByBlack;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MobilityCount)) return false;

        MobilityCount that = (MobilityCount) o;

        if (emptySquares != that.emptySquares) return false;
        if (occupiedByWhite != that.occupiedByWhite) return false;
        return getOccupiedByBlack == that.getOccupiedByBlack;

    }

    @Override
    public int hashCode() {
        int result = emptySquares;
        result = 31 * result + occupiedByWhite;
        result = 31 * result + getOccupiedByBlack;
        return result;
    }
}
