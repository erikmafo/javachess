package com.erikmafo.chess.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by erikmafo on 13.11.16.
 */
public class SquareView extends Rectangle {

    public SquareView(boolean light, int x, int y) {


        setWidth(ChessApplication.SQUARE_SIZE);
        setHeight(ChessApplication.SQUARE_SIZE);

        relocate(x * ChessApplication.SQUARE_SIZE, y * ChessApplication.SQUARE_SIZE);

        setFill(light ? Color.valueOf("#feb") : Color.valueOf("582"));
    }
}
