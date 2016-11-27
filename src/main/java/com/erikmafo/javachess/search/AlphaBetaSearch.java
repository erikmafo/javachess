package com.erikmafo.javachess.search;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.movegenerator.BoardSeeker;
import com.erikmafo.javachess.movegenerator.MoveGenerator;
import com.erikmafo.javachess.pieces.PieceColor;

import java.util.List;

public class AlphaBetaSearch implements MoveSearch {

    private static final int MAX_VALUE = 1000000;

    private final MoveGenerator moveGenerator;
    private final BoardSeeker boardSeeker;

    public AlphaBetaSearch(MoveGenerator moveGenerator, BoardSeeker boardSeeker) {
        this.moveGenerator = moveGenerator;
        this.boardSeeker = boardSeeker;
    }

    @Override
    public SearchResult execute(Board board, BoardToIntFunction boardToIntFunction, int depth) {

        Move[] principleVariation = new Move[depth];

        int alpha = -MAX_VALUE;
        int beta = MAX_VALUE;
        int score = negMax(board, boardToIntFunction, principleVariation, alpha, beta, depth);

        return new SearchResult(score, principleVariation);
    }

    private int negMax(Board board, BoardToIntFunction evaluation, Move[] principleVariation, int alpha, int beta, int depthLeft) {

        if (depthLeft == 0 || Thread.currentThread().isInterrupted()) {
            return evaluation.applyAsInt(board);
        }

        List<Move> moves = board.getMoves(moveGenerator);

        PieceColor color = board.getColorToMove();

        for (Move move : moves) {

            move.play();

            if (!isChecked(color, board)) {
                int score = - negMax(board, evaluation, principleVariation, -beta, -alpha, depthLeft - 1);
                if (score >= beta) {
                    principleVariation[principleVariation.length - depthLeft] = move;
                    move.undo();
                    return beta;
                }
                if (score > alpha) {
                    principleVariation[principleVariation.length - depthLeft] = move;
                    alpha = score;
                }
            }

            move.undo();
        }


        return alpha;
    }

    boolean isChecked(PieceColor color, Board board) {

        BoardCoordinate square = board.getKingLocation(color);

        return boardSeeker.isAttackedBy(color.getOpposite(), square, board);
    }
}