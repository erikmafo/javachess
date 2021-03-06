package com.erikmafo.chess.search.evaluation;

import com.erikmafo.chess.board.BasicOffset;
import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.KnightOffset;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.movegenerator.BoardSeeker;
import com.erikmafo.chess.movegenerator.MobilityCount;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;

import java.util.Iterator;

/**
 * Created by erikmafo on 11.12.16.
 */
public class MobilityEvaluation implements BoardToIntFunction {

    private final BoardSeeker boardSeeker;

    public MobilityEvaluation(BoardSeeker boardSeeker) {
        this.boardSeeker = boardSeeker;
    }

    public MobilityEvaluation() {
        this(new BoardSeeker());
    }


    @Override
    public int applyAsInt(Board board) {

        PieceColor colorToMove = board.getColorToMove();

        Iterator<Square> squareIterator = board.occupiedSquareIterator();

        int score = 0;

        Square current;

        while (squareIterator.hasNext()) {

            current = squareIterator.next();
            Piece piece = board.getNullablePiece(current);
            PieceType type = piece.getType();

            MobilityCount mobilityCount = null;

            /**f (PieceType.QUEEN.equals(type)) {
                mobilityCount = boardSeeker.getMobilityCount(board, current, true, BasicOffset.values());
            } else */

            if (PieceType.ROOK.equals(type)) {
                mobilityCount = boardSeeker.getMobilityCount(board, current, true, BasicOffset.rookValues());
            } else if (PieceType.BISHOP.equals(type)) {
                mobilityCount = boardSeeker.getMobilityCount(board, current, true, BasicOffset.bishopValues());
            } else if (PieceType.KNIGHT.equals(type)) {
                mobilityCount = boardSeeker.getMobilityCount(board, current, false, KnightOffset.values());
            }

            if (mobilityCount != null) {
                int mod = colorToMove.equals(piece.getColor()) ? 1 : -1;
                score += mod * (mobilityCount.getEmptySquares() + 2 * mobilityCount.getOccupiedBy(colorToMove.opponent()));
            }
        }

        return score;
    }





}
