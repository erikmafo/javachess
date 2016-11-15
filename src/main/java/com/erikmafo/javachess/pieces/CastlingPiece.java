package com.erikmafo.javachess.pieces;

import com.erikmafo.javachess.boardrep.BoardCoordinate;

/**
 * A castling piece is like an ordinary Piece, but keeps track of whether it hasMoved.
 */
public class CastlingPiece extends Piece {

    private int moveCount = 0;

    public CastlingPiece(PieceColor pieceColor, PieceType pieceType, BoardCoordinate coordinate) {
        super(pieceColor, pieceType, coordinate);
    }

    @Override
    public void move(BoardCoordinate newCoordinate) {
        super.move(newCoordinate);
        moveCount++;
    }

    @Override
    public void moveBackwards(BoardCoordinate oldCoordinate) {
        super.move(oldCoordinate);
        moveCount--;
    }


    public boolean hasMoved() {
        return moveCount > 0;
    }
}
