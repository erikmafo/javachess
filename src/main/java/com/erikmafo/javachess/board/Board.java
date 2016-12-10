package com.erikmafo.javachess.board;

import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.movegenerator.MoveGenerationStrategy;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;

import java.util.*;

/**
 * Created by erikmafo on 19.11.16.
 */
public interface Board {

    PieceColor getColorToMove();

    Square getKingLocation(PieceColor kingColor);

    Optional<Piece> pieceAt(Square square);

    Piece getNullablePiece(Square square);

    boolean hasCastled(PieceColor color);

    Optional<Square> enPassentTarget();

    boolean isOccupied(Square square);

    int getMoveCount();

    List<Move> getMoves(MoveGenerationStrategy moveGenerationStrategy);

    boolean hasKingSideCastlingRight(PieceColor pieceColor);

    boolean hasQueenSideCastlingRight(PieceColor pieceColor);

    Collection<Square> getOccupiedSquares();

    Iterator<Square> occupiedSquareIterator();

    Iterator<Piece> pieceIterator();




}
