package com.erikmafo.javachess.moves;

import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.Offset;
import com.erikmafo.javachess.boardrep.PlayableBoard;
import com.erikmafo.javachess.boardrep.ReadableBoard;
import com.erikmafo.javachess.pieces.PieceType;
import com.erikmafo.javachess.search.MoveSearchAlgorithm;
import com.erikmafo.javachess.search.MinMax;
import com.erikmafo.javachess.search.SimpleMaterialEvaluationFunction;

import java.util.HashMap;
import java.util.Map;


public class Moves {

    private Moves() {
    }

    private static final Map<Index, Move> quietMoves = new HashMap<>();
    private static final Map<Index, Move> captureMoves = new HashMap<>();
    private static Index index = new Index();

    public static Move valueOf(ReadableBoard board, BoardCoordinate from, BoardCoordinate to) {
        Move move;
        PieceType type = board.getPieceTypeAt(from);

        if (type == PieceType.KING) {
            move = valueOfKingMove(board, from, to);
        } else if (type == PieceType.PAWN) {
            move = valueOfPawnMove(board, from, to);
        } else if (board.isOccupiedAt(to)) {
            move = createCaptureMove(from, to);
        } else {
            move = createQuietMove(from, to);
        }
        return move;
    }

    public static Move createCaptureMove(BoardCoordinate from, BoardCoordinate to) {
        index.set(from, to);
        Move move = captureMoves.get(index);
        if (move == null) {
            move = new CaptureMove(from, to);
            captureMoves.put(new Index(from, to), move);
        }
        return move;
    }

    public static Move createQuietMove(BoardCoordinate from, BoardCoordinate to) {
        index.set(from, to);
        Move move = quietMoves.get(index);
        if (move == null) {
            move = new QuietMove(from, to);
            quietMoves.put(new Index(from, to), move);
        }
        return move;
    }

    public static Move createEnPassentMove(BoardCoordinate from, BoardCoordinate to) {
        return new EnPassentMove(from, to);
    }

    public static Move createPawnPromotionMove(BoardCoordinate from, BoardCoordinate to, PieceType newPieceType) {
        return new PawnPromotionMove(from, to, newPieceType);
    }


    private static Move valueOfPawnMove(ReadableBoard board, BoardCoordinate from, BoardCoordinate to) {
        Move move;
        if (board.isOccupiedAt(to)) {
            move = createCaptureMove(from, to);
        } else if (to.getFile() - from.getFile() != 0) {
            move = createEnPassentMove(from, to);
        } else if (to.getRank() == BoardCoordinate.A8.getRank() ||
                to.getRank() == BoardCoordinate.A1.getRank()) {
            move = createPawnPromotionMove(from, to, PieceType.QUEEN);
        } else {
            move = createQuietMove(from, to);
        }
        return move;
    }


    private static Move valueOfKingMove(ReadableBoard board, BoardCoordinate from, BoardCoordinate to) {
        Move move;

        if (2 == to.getFile() - from.getFile()) {
            // king side castling move
            BoardCoordinate rookFrom = to.next(Offset.RIGHT);
            BoardCoordinate rookTo = from.next(Offset.RIGHT);
            move = createCastlingMove(from, to, rookFrom, rookTo);
        } else if (2 == from.getFile() - to.getFile()) {
            // queen side castling move
            BoardCoordinate rookFrom = to.next(Offset.LEFT).next(Offset.LEFT);
            BoardCoordinate rookTo = from.next(Offset.LEFT);
            move = createCastlingMove(from, to, rookFrom, rookTo);
        } else if (board.isOccupiedAt(to)) {
            move = createCaptureMove(from, to);
        } else {
            move = createQuietMove(from, to);
        }

        return move;
    }


    public static Move findBestMove(PlayableBoard board) {
        SimpleMaterialEvaluationFunction evaluationFunction = SimpleMaterialEvaluationFunction.getInstance();
        evaluationFunction.setColor(board.getMovingColor());
        MoveSearchAlgorithm moveSearchAlgorithm = new MinMax();

        return moveSearchAlgorithm.execute(board, evaluationFunction, 5);
    }

    public static Move createCastlingMove(BoardCoordinate kingFrom, BoardCoordinate kingTo,
                                          BoardCoordinate rookFrom, BoardCoordinate rookTo) {
        return new CastlingMove(kingFrom, kingTo, rookFrom, rookTo);
    }


    private static class Index {
        BoardCoordinate from;
        BoardCoordinate to;

        Index(BoardCoordinate from, BoardCoordinate to) {
            this.from = from;
            this.to = to;
        }

        public Index() {
        }

        void set(BoardCoordinate from, BoardCoordinate to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public int hashCode() {
            return from.hashCode() ^ to.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Index) {
                return from == ((Index) obj).from && to == ((Index) obj).to;
            }
            return false;
        }

    }

}





