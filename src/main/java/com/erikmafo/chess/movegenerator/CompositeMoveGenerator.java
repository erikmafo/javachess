package com.erikmafo.chess.movegenerator;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;

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
