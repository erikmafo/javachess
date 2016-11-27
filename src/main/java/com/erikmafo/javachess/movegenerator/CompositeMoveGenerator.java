package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.*;

/**
 * Created by erikmafo on 20.11.16.
 */
public class CompositeMoveGenerator implements MoveGenerator {


    private final Map<PieceType, MoveGenerator> moveGenerators;

    public CompositeMoveGenerator(Map<PieceType, MoveGenerator> moveGenerators) {
        this.moveGenerators = new EnumMap<>(moveGenerators);
    }

    @Override
    public List<Move> generateMoves(Board board, BoardCoordinate from, MoveFactory moveFactory) {

        List<Move> moves = new ArrayList<>();

        PieceColor color = board.getColorToMove();

        Optional<Piece> pieceOptional = board.pieceAt(from);

        if (pieceOptional.filter(piece -> piece.getColor().equals(color)).isPresent()) {
            PieceType type = pieceOptional.get().getType();
            moves.addAll(moveGenerators.get(type).generateMoves(board, from, moveFactory));
        }

        return moves;
    }

    public static class Builder {

        private final Map<PieceType, MoveGenerator> moveGenerators = new HashMap<>();

        public Builder setMoveGenerator(PieceType pieceType, MoveGenerator moveGenerator) {
            moveGenerators.put(pieceType, moveGenerator);
            return this;
        }

        public MoveGenerator build() {
            return new CompositeMoveGenerator(moveGenerators);
        }

    }







}
