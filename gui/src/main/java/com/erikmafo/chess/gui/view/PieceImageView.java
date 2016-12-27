package com.erikmafo.chess.gui.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;

/**
 * Created by erikmafo on 27.12.16.
 */
public class PieceImageView extends ImageView {


    private final String[] validColors = {"black", "white"};
    private final String[] validPieceTypes = {"pawn", "bishop", "knight", "rook", "queen", "king"};

    private final String color;
    private final String pieceType;


    public PieceImageView(PieceImageView other) {
        this.color = other.getColor();
        this.pieceType = other.getPieceType();
        setImage(other.getImage());
    }

    public PieceImageView(String color, String pieceType) {
        validateArguments(color, pieceType);
        this.color = color.toLowerCase();
        this.pieceType = pieceType.toLowerCase();
        Image image = loadImage(this.color, this.pieceType);
        setImage(image);
    }

    private void validateArguments(String color, String pieceType) {
        if (!Arrays.asList(validColors).contains(color.toLowerCase())) {
            throw new IllegalArgumentException("Unkown piece color");
        }

        if (!Arrays.asList(validPieceTypes).contains(pieceType.toLowerCase())) {
            throw new IllegalArgumentException("Unknown piece type");
        }
    }


    private Image loadImage(String color, String pieceType) {
        return new Image(getClass().getResourceAsStream("/images/pieces/" + color + "_" + pieceType + ".png"));
    }

    public String getColor() {
        return color;
    }

    public String getPieceType() {
        return pieceType;
    }
}
