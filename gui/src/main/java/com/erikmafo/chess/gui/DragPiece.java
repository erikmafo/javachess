package com.erikmafo.chess.gui;

import com.erikmafo.chess.board.Square;


public class DragPiece {

    Square square;

    private boolean isSelected;

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public Square getCoordinate() {
        return square;
    }
}
