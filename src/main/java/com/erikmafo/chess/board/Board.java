package com.erikmafo.chess.board;

import com.erikmafo.chess.pieces.Piece;
import com.erikmafo.chess.pieces.PieceColor;

import java.util.*;

/**
 * Created by erikmafo on 19.11.16.
 */
public interface Board extends MoveReceiver {

    PieceColor getColorToMove();

    Square getKingLocation(PieceColor kingColor);

    Optional<Piece> pieceAt(Square square);

    Piece getNullablePiece(Square square);

    boolean hasCastled(PieceColor color);

    Optional<Square> enPassentTarget();

    boolean isOccupied(Square square);

    int getMoveCount();

    boolean hasKingSideCastlingRight(PieceColor pieceColor);

    boolean hasQueenSideCastlingRight(PieceColor pieceColor);

    Collection<Square> getOccupiedSquares();

    Iterator<Square> occupiedSquareIterator();

    Iterator<Piece> pieceIterator();


    long getTranspositionKey();




}
