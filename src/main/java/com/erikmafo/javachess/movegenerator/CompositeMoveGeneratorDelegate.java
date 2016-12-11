package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.Square;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.*;

/**
 * Created by erikmafo on 20.11.16.
 */
public class CompositeMoveGeneratorDelegate implements MoveGeneratorDelegate {


    private final Map<PieceType, MoveGeneratorDelegate> moveGenerators;

    public CompositeMoveGeneratorDelegate(Map<PieceType, MoveGeneratorDelegate> moveGenerators) {
        this.moveGenerators = new EnumMap<>(moveGenerators);
    }

    @Override
    public List<Move> generateMoves(Board board, Square from) {

        List<Move> moves = new ArrayList<>();

        PieceColor color = board.getColorToMove();

        Optional<Piece> pieceOptional = board.pieceAt(from);

        if (pieceOptional.filter(piece -> piece.getColor().equals(color)).isPresent()) {
            PieceType type = pieceOptional.get().getType();
            moves.addAll(moveGenerators.get(type).generateMoves(board, from));
        }

        return moves;
    }

    public static class Builder {

        private final Map<PieceType, MoveGeneratorDelegate> moveGenerators = new HashMap<>();

        public Builder setMoveGenerator(PieceType pieceType, MoveGeneratorDelegate moveGeneratorDelegate) {
            moveGenerators.put(pieceType, moveGeneratorDelegate);
            return this;
        }

        public MoveGeneratorDelegate build() {
            return new CompositeMoveGeneratorDelegate(moveGenerators);
        }

    }







}
