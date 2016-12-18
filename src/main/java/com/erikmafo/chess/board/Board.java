package com.erikmafo.chess.board;

import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;

import java.util.*;

/**
 * @author Erik Folstad
 */
public interface Board extends MoveReceiver {

    /**
     * Gets the color of pieces that will perform the next move.
     *
     * @return a <Code>PieceColor</Code>
     */
    PieceColor getColorToMove();

    /**
     * Gets the location of the king with the specified color.
     *
     * @param kingColor the color of the king who's location is to be determined
     * @return the square where the king of the specified color is placed, or <Code>Square.OFF_BOARD</Code> if not
     * present
     */
    Square getKingLocation(PieceColor kingColor);

    /**
     * Gets the piece at the specified square.
     *
     * @param square the square
     * @return the piece that is placed on the specified square, or null if no piece is present
     */
    Piece getNullablePiece(Square square);

    /**
     * Gets the piece at the specified square wrapped in an {@Link Optional}.
     *
     * @param square
     * @return
     */
    Optional<Piece> pieceAt(Square square);

    /**
     * Returns whether the specified color has castled
     *
     * @param color
     * @return true if the specified color has castled, or false otherwise
     */
    boolean hasCastled(PieceColor color);


    Optional<Square> enPassentTarget();

    /**
     * Returns whether the specified square is occupied by a {@Link Piece}.
     *
     * @param square
     * @return true if a piece is placed at the specified square, or false otherwise.
     */
    boolean isOccupied(Square square);

    /**
     * Gets the total number of moves that has been played on this board.
     *
     * @return
     */
    int getMoveCount();


    boolean hasKingSideCastlingRight(PieceColor pieceColor);

    boolean hasQueenSideCastlingRight(PieceColor pieceColor);

    Collection<Square> getOccupiedSquares();

    /**
     * Gets an iterator over all the squares of this Board that is occupied by a piece
     *
     * @return an iterator over all occupied squares
     */
    Iterator<Square> occupiedSquareIterator();


    Iterator<Piece> pieceIterator();

    /**
     * Gets the current transposition key of this board. This key may be useful
     * for caching information related to the position that is currently on the board.
     *
     * <br/>
     *
     * The key is not guaranteed to be unique for each position, but given two different arbitrary positions
     * the probability that the transposition keys coincide should be very small.
     *
     * @return the transposition key of the current position on this board
     */
    long getTranspositionKey();

}
