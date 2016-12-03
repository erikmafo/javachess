package com.erikmafo.javachess.board;

import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.movegenerator.MoveGenerationStrategy;
import com.erikmafo.javachess.movegenerator.MoveGenerator;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;

import java.util.List;
import java.util.Optional;

/**
 * Created by erikmafo on 19.11.16.
 */
public interface Board {

    PieceColor getColorToMove();

    BoardCoordinate getKingLocation(PieceColor kingColor);

    Optional<Piece> pieceAt(BoardCoordinate boardCoordinate);

    Piece getNullablePiece(BoardCoordinate boardCoordinate);

    boolean hasCastled(PieceColor color);

    Optional<BoardCoordinate> enPassentTarget();

    boolean isOccupied(BoardCoordinate boardCoordinate);

    int getMoveCount();

    List<Move> getMoves(MoveGenerationStrategy moveGenerationStrategy);

    boolean hasKingSideCastlingRight(PieceColor pieceColor);

    boolean hasQueenSideCastlingRight(PieceColor pieceColor);



}
