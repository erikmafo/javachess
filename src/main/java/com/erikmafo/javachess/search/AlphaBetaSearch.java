package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.movegenerator.MoveGenerationStrategy;
import com.erikmafo.javachess.movegenerator.MoveGenerator;
import com.erikmafo.javachess.movegenerator.MoveGeneratorFactory;
import com.erikmafo.javachess.search.evaluation.BoardToIntFunction;

import java.util.Arrays;
import java.util.List;

public class AlphaBetaSearch implements MoveSearch {

    private static final int MAX_VALUE = 1000000;


    private final MoveGenerator moveGenerator;
    private final TranspositionTable transpositionTable;

    public AlphaBetaSearch() {
        this(getDefaultMoveGenerator(), new TranspositionTable());
    }

    private static MoveGenerator getDefaultMoveGenerator() {
        return new MoveGeneratorFactory().newInstance(MoveGenerationStrategy.ALL_PSEUDO_LEGAL_MOVES, new MoveFactory());
    }

    public AlphaBetaSearch(MoveGenerator moveGenerator, TranspositionTable transpositionTable) {
        this.moveGenerator = moveGenerator;
        this.transpositionTable = transpositionTable;
    }


    @Override
    public SearchResult execute(Board board, BoardToIntFunction boardToIntFunction, int depth) {

        Move[] principleVariation = new Move[depth];

        int alpha = -MAX_VALUE;
        int beta = MAX_VALUE;
        int score = negMax(principleVariation, board, boardToIntFunction, alpha, beta, depth);

        return new SearchResult(score, principleVariation);
    }

    @Override
    public SearchResult execute(Board board, BoardToIntFunction boardToIntFunction, int depth, List<Move> principleVariation) {
        Move[] principleVariationArray = Arrays.copyOf(principleVariation.toArray(new Move[0]), depth);

        int alpha = -MAX_VALUE;
        int beta = MAX_VALUE;
        int score = negMax(principleVariationArray, board, boardToIntFunction, alpha, beta, depth);

        return new SearchResult(score, principleVariationArray);
    }

    private int negMax(Move[] principleVariation, Board board, BoardToIntFunction evaluation, int alpha, int beta, int depthLeft) {


        List<Move> moves = moveGenerator.generateMoves(board);

        long key = board.getTranspositionKey();
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
            return evaluation.applyAsInt(board);
        }

        sortMoves(moves, principleVariation[principleVariation.length - depthLeft]);

        Move bestMove = moves.get(0);
        int bestValue = -MAX_VALUE;

        for (Move move : moves) {

            move.play();
            int score = -negMax(principleVariation, board, evaluation, -beta, -alpha, depthLeft - 1);
            move.undo();

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

            return Integer.compare(o2.getRank(), o1.getRank());
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