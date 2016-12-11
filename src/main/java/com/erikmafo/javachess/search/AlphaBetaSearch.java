package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.movegenerator.MoveGenerationStrategy;
import com.erikmafo.javachess.movegenerator.MoveGenerator;
import com.erikmafo.javachess.movegenerator.MoveGeneratorFactory;

import java.util.Arrays;
import java.util.List;

public class AlphaBetaSearch implements MoveSearch {

    private static final int MAX_VALUE = 1000000;


    private final MoveGenerator moveGenerator;

    public AlphaBetaSearch() {
        this(new MoveGeneratorFactory().newInstance(MoveGenerationStrategy.ALL_PSEUDO_LEGAL_MOVES, new MoveFactory()));
    }

    public AlphaBetaSearch(MoveGenerator moveGenerator) {
        this.moveGenerator = moveGenerator;
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

        if (depthLeft == 0 || Thread.currentThread().isInterrupted()) {
            return evaluation.applyAsInt(board);
        }

        List<Move> moves = moveGenerator.generateMoves(board);

        Move assumedBest = principleVariation[principleVariation.length - depthLeft];


        moves.sort((o1, o2) -> {
            if (o1.equals(assumedBest)) {
                return -1;
            }
            if (o2.equals(assumedBest)) {
                return 1;
            }
            return Integer.compare(o2.getRank(), o1.getRank());
        });


        for (Move move : moves) {

            move.play();


            int score = -negMax(principleVariation, board, evaluation, -beta, -alpha, depthLeft - 1);
            if (score >= beta) {
                principleVariation[principleVariation.length - depthLeft] = move;
                move.undo();
                return beta;
            }
            if (score > alpha) {
                principleVariation[principleVariation.length - depthLeft] = move;
                alpha = score;
            }

            move.undo();
        }


        return alpha;
    }

}