package com.erikmafo.chess.board;

import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;

/**
 * Represents the target for a {@link Move}
 *
 * The move receiver is responsible for updating the chess board
 */
public interface MoveReceiver {

    /**
     * Moves the piece that occupies the first square to the second square. If no piece occupies the first square
     * then this method may throw a {@link NullPointerException}.
     *
     * @param from the start square of the moving piece
     * @param to the destination square of the moving piece
     */
    void movePiece(Square from, Square to);

    /**
     * Removes the piece at the specified square.
     *
     * @param square the square of the piece to be removed
     * @return the piece that occupied the specified quare, or null if the square was empty.
     */
    Piece remove(Square square);

    /**
     * Places a piece at the specified square.
     *
     * @param square the square that the piece should be placed
     * @param piece the piece to place
     * @return the piece that previously occupied the square
     */
    Piece put(Square square, Piece piece);

    /**
     * Is called when a double pawn push move is played, such that an en passent target is created for the next turn.
     *
     * @param square the en passent target
     */
    void setEnPassentTarget(Square square);

    /**
     * Removes any en passent target that is present for the next turn.
     */
    void removeEnPassentTarget();

    /**
     * Sets whether a side has castled
     *
     * @param color the color of the side that has castled
     * @param hasCastled true if the side has castled, false otherwise
     */
    void setHasCastled(PieceColor color, boolean hasCastled);

    /**
     * Resets the halfmove clock.
     * <br/>
     * The halfmove clock inside an chess position object takes care of enforcing the fifty-move rule. This counter
     * is reset after captures or pawn moves, and incremented otherwise. Also moves which lose the castling rights,
     * that is rook- and king moves from their initial squares, including castling itself, increment the halfmove clock.
     */
    void resetHalfMoveClock();

    /**
     * Completes the current turn and makes this <code>MoveReceiver</code> ready to receive a new {@link Move}.
     */
    void completePlay();

    /**
     * Is called when a {@link Move} is reversed. This brings the move receiver back to the previous turn.
     */
    void completeUndo();

}
