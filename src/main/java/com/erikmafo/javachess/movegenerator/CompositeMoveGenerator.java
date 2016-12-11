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
public class CompositeMoveGenerator implements MoveGenerator {


    private final Map<PieceType, MoveGeneratorDelegate> moveGeneratorsDelegates;

    CompositeMoveGenerator(Map<PieceType, MoveGeneratorDelegate> moveGeneratorsDelegates) {
        this.moveGeneratorsDelegates = new EnumMap<>(moveGeneratorsDelegates);
    }

    @Override
    public List<Move> generateMoves(Board board) {

        List<Move> moves = new ArrayList<>();

        PieceColor color = board.getColorToMove();

        Iterator<Square> occupiedSquares = board.occupiedSquareIterator();

        Square from;

        while (occupiedSquares.hasNext()) {

            from = occupiedSquares.next();
            Piece piece = board.getNullablePiece(from);
            if (color.equals(piece.getColor())) {
                moves.addAll(geneerateMoves(board, from, piece));
            }

        }

        return moves;
    }

    private List<Move> geneerateMoves(Board board, Square from, Piece piece) {
        return moveGeneratorsDelegates.get(piece.getType()).generateMoves(board, from);
    }

    public static class Builder {

        private final Map<PieceType, MoveGeneratorDelegate> moveGenerators = new HashMap<>();

        public Builder setMoveGenerator(PieceType pieceType, MoveGeneratorDelegate moveGeneratorDelegate) {
            moveGenerators.put(pieceType, moveGeneratorDelegate);
            return this;
        }

        public MoveGenerator build() {
            return new CompositeMoveGenerator(moveGenerators);
        }

    }



}
