package com.erikmafo.chess.search.evaluation;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;

import java.util.Iterator;

/**
 * Created by erikf on 12/17/2016.
 */
public class PositionalEvaluation implements BoardToIntFunction {

    private final PositionalEvaluationTable evaluationTable;

    public PositionalEvaluation(PositionalEvaluationTable evaluationTable) {
        this.evaluationTable = evaluationTable;
    }

    public PositionalEvaluation() {
        this(new PositionalEvaluationTable());
    }

    @Override
    public int applyAsInt(Board board) {

        int score = 0;

        PieceColor colorToMove = board.getColorToMove();

        Iterator<Square> squareIterator = board.occupiedSquareIterator();

        while (squareIterator.hasNext()) {

            Square current = squareIterator.next();

            Piece piece = board.getNullablePiece(current);

            int mod = colorToMove.equals(piece.getColor()) ? 1 : -1;

            score += mod * evaluationTable.getPositionalScore(piece, current, PositionalEvaluationTable.GamePhase.MIDDLE_GAME);
        }


        return score;
    }
}
