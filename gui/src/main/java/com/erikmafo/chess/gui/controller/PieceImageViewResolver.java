package com.erikmafo.chess.gui.controller;

import com.erikmafo.chess.gui.components.PieceImageView;
import com.erikmafo.chess.piece.Piece;

public class PieceImageViewResolver {
    public PieceImageViewResolver() {
    }

    PieceImageView getPieceImageView(Piece piece) {
        return new PieceImageView("" + piece.getColor(), "" + piece.getType());
    }
}