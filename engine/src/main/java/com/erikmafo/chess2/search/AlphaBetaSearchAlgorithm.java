package com.erikmafo.chess2.search;


import com.erikmafo.chess2.evaluation.EvaluationFunction;
import com.erikmafo.chess2.movegeneration.Move;
import com.erikmafo.chess2.movegeneration.Board;

import java.util.List;

/**
 * Created by erikmafo on 19.11.17.
 */
public class AlphaBetaSearchAlgorithm implements SearchAlgorithm {
    private static final int MAX_VALUE = 1000000;

    private final TranspositionTable transpositionTable;
    private final EvaluationFunction evaluationFunction;

    public AlphaBetaSearchAlgorithm(TranspositionTable transpositionTable, EvaluationFunction evaluationFunction) {
        this.transpositionTable = transpositionTable;
        this.evaluationFunction = evaluationFunction;
    }

    @Override
    public SearchResult execute(Board board, int depth) {

        Move[] principleVariation = new Move[depth];

        int alpha = -MAX_VALUE;
        int beta = MAX_VALUE;
        int score = negMax(principleVariation, board, board.generateMoves(), evaluationFunction, alpha, beta, depth);

        return new SearchResult(score, principleVariation);
    }

    private int negMax(Move[] principleVariation, Board board, List<Move> moves, EvaluationFunction evaluation, int alpha, int beta, int depthLeft) {

        long key = board.hash();
        Transposition transposition = transpositionTable.retrieve(key);

        if (transposition != null && transposition.getDepth() >= depthLeft) {

            switch (transposition.getFlag()) {
                case EXACT:
                    // verify move is legal - might be hash collision
                    if (moves.contains(transposition.getBestMove())) {
                        principleVariation[principleVariation.length - depthLeft] = transposition.getBestMove();
                        return transposition.getScore();
                    }
                case UPPER_BOUND:
                    beta = Math.min(beta, transposition.getScore());
                    break;
                case LOWER_BOUND:
                    alpha = Math.max(alpha, transposition.getScore());
                    break;
            }

            if (alpha >= beta) {
                //principleVariation[principleVariation.length - depthLeft] = transposition.getBestMove();
                return transposition.getScore();
            }
        }

        if (depthLeft == 0 || Thread.currentThread().isInterrupted()) {
            return evaluation.evaluate(board);
        }

        sortMoves(moves, principleVariation[principleVariation.length - depthLeft]);

        Move bestMove = moves.get(0);
        int bestValue = -MAX_VALUE;

        for (Move move : moves) {

            board.play(move);
            int score = -negMax(principleVariation, board, board.generateMoves(), evaluation, -beta, -alpha, depthLeft - 1);
            board.undoLastMove();

            if (score > bestValue) {
                bestValue = score;
                bestMove = move;
            }

            if (score > alpha) {
                alpha = score;
            }

            if (alpha >= beta) {
                break;
            }
        }

        principleVariation[principleVariation.length - depthLeft] = bestMove;

        updateTranspositionTable(key, alpha, beta, depthLeft, bestValue, bestMove);

        return bestValue;
    }

    private void sortMoves(List<Move> moves, Move assumedBest) {
        moves.sort((o1, o2) -> {


            if (o1.equals(assumedBest)) {
                return -1;
            }

            if (o2.equals(assumedBest)) {
                return 1;
            }

            return o1.compareTo(o2);
        });
    }

    private void updateTranspositionTable(long key, int alpha, int beta, int depth, int bestValue, Move bestMove) {
        if (bestValue <= alpha) {
            updateTranspositionTable(key, bestValue, bestMove, Transposition.Flag.LOWER_BOUND, depth);
        } else if (bestValue >= beta) {
            updateTranspositionTable(key, bestValue, bestMove, Transposition.Flag.UPPER_BOUND, depth);
        } else {
            updateTranspositionTable(key, bestValue, bestMove, Transposition.Flag.EXACT, depth);
        }
    }

    private void updateTranspositionTable(long key, int score, Move bestMove, Transposition.Flag flag, int depth) {
        transpositionTable.store(new Transposition(flag, key, score, depth, bestMove));
    }
}
