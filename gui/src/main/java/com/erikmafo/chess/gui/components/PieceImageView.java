package com.erikmafo.chess.gui.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by erikmafo on 27.12.16.
 */
public class PieceImageView extends ImageView {


    private final String[] validColors = {"black", "white"};
    private final String[] validPieceTypes = {"pawn", "bishop", "knight", "rook", "queen", "king"};

    private final StringProperty color;
    private final String pieceType;

    private final BooleanProperty isDraggable = new SimpleBooleanProperty();


    public PieceImageView(PieceImageView other) {
        this.color = new SimpleStringProperty(other.getColor());
        this.pieceType = other.getPieceType();
        setImage(other.getImage());
    }

    public PieceImageView(String color, String pieceType) {
        validateArguments(color, pieceType);
        this.color = new SimpleStringProperty(color.toLowerCase());
        this.pieceType = pieceType.toLowerCase();
        Image image = loadImage(this.color.get(), this.pieceType);
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

    public boolean isDraggable() {
        return isDraggable.get();
    }

    public BooleanProperty isDraggableProperty() {
        return isDraggable;
    }

    public void setIsDraggable(boolean isDraggable) {
        this.isDraggable.set(isDraggable);
    }

    public String getColor() {
        return color.get();
    }

    public StringProperty colorProperty() {
        return color;
    }

    public String getPieceType() {
        return pieceType;
    }


    public boolean isSamePiece(PieceImageView other) {
        return Objects.equals(color, other.color) && Objects.equals(color, other.color);
    }
}
