package com.erikmafo.javachess.search;

import com.erikmafo.javachess.boardrep.PlayableBoard;
import com.erikmafo.javachess.moves.Move;
import com.erikmafo.javachess.pieces.PieceColor;

import java.util.ArrayList;
import java.util.List;


public class MinMax extends MoveSearchAlgorithm {

    private static final int MAX_DEPTH = 8;

    private final Move[] principleVariation = new Move[MAX_DEPTH];
    private final MoveComparator moveComparator = new MoveComparator();

    private int depth;


    protected int max(PlayableBoard board,
                      EvaluationFunction evaluationFunction, int alpha, int beta, int depthLeft) {

        if (depthLeft == 0) {
            return evaluationFunction.evaluate(board);
        }

        List<Move> moves = new ArrayList<>();
        board.fillWithPossibleMoves(moves);
        moves.sort(moveComparator.reversed());


        PieceColor color = board.getMovingColor();

        for (Move move : moves) {

            board.play(move);
            if (!board.isChecked(color)) {
                int score = min(board, evaluationFunction, alpha, beta, depthLeft - 1);
                if (score >= beta) {
                    principleVariation[depth - depthLeft] = move;
                    board.undoLast();
                    return beta;
                }
                if (score > alpha) {
                    principleVariation[depth - depthLeft] = move;
                    alpha = score;
                }
            }
            board.undoLast();
        }

        return alpha;
    }


    protected int min(PlayableBoard board,
                      EvaluationFunction evaluationFunction, int alpha, int beta, int depthLeft) {
        if (depthLeft == 0) {
            return evaluationFunction.evaluate(board);
        }

        List<Move> moves = new ArrayList<>();
        board.fillWithPossibleMoves(moves);
        moves.sort(moveComparator.reversed());

        PieceColor color = board.getMovingColor();

        for (Move move : moves) {
            board.play(move);
            if (!board.isChecked(color)) { // move is legal
                int score = max(board, evaluationFunction, alpha, beta, depthLeft - 1);
                if (score <= alpha) {
                    principleVariation[depth - depthLeft] = move;
                    board.undoLast();
                    return alpha;
                }
                if (score < beta) {
                    beta = score;
                    principleVariation[depth - depthLeft] = move;
                }
            }
            board.undoLast();
        }

        return beta;
    }

    @Override
    public Move execute(PlayableBoard board,
                        EvaluationFunction evaluationFunction, int depth) {

        if (depth > MAX_DEPTH) {
            throw new IllegalArgumentException("depth exceeds MAX_DEPTH");
        }

        this.depth = depth;
        max(board, evaluationFunction, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);


        return principleVariation[0];

    }


}





