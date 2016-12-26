package com.erikmafo.chess.gui;

import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import javafx.scene.image.ImageView;

/**
 * Created by erikmafo on 16.11.16.
 */
public class PieceEntry {

    private final PieceColor pieceColor;
    private final PieceType pieceType;
    private final ImageView pieceImageView;


    public PieceEntry(PieceColor pieceColor, PieceType pieceType, ImageView pieceImageView) {
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        this.pieceImageView = pieceImageView;
    }


    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public ImageView getPieceImageView() {
        return pieceImageView;
    }
}