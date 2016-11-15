package com.erikmafo.javachess.ui;

import com.erikmafo.javachess.boardrep.BoardCoordinate;


public class DragPiece {

    BoardCoordinate boardCoordinate;

    private boolean isSelected;

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setBoardCoordinate(BoardCoordinate boardCoordinate) {
        this.boardCoordinate = boardCoordinate;
    }

    public BoardCoordinate getCoordinate() {
        return boardCoordinate;
    }
}
