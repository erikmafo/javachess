package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Offset;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikmafo on 20.11.16.
 */
public class MoveGenerators {


    public static MoveGenerator newPseudoLegalMoveGenerator() {

        MoveGenerator moveGenerator = new CompositeMoveGenerator.Builder()
                .setMoveGenerator(PieceType.PAWN, getPawnMoveGenerator())
                .setMoveGenerator(PieceType.BISHOP, getBishopMoveGenerator())
                .setMoveGenerator(PieceType.ROOK, getRookMoveGenerator())
                .setMoveGenerator(PieceType.KNIGHT, getKnightMoveGenerator())
                .setMoveGenerator(PieceType.QUEEN, getQueenMoveGenerator())
                .setMoveGenerator(PieceType.KING, getKingMoveGenerator())
                .build();


        return moveGenerator;
    }

    private static PawnMoveGenerator getPawnMoveGenerator() {
        return new PawnMoveGenerator();
    }

    private static MoveGenerator getQueenMoveGenerator() {
        return new SlidingMoveGenerator(
                Offset.DOWN_LEFT, Offset.DOWN_RIGHT, Offset.UP_LEFT, Offset.UP_RIGHT,
                Offset.UP, Offset.DOWN, Offset.RIGHT, Offset.LEFT);
    }

    private static MoveGenerator getBishopMoveGenerator() {
        return new SlidingMoveGenerator(
                Offset.DOWN_LEFT, Offset.DOWN_RIGHT, Offset.UP_LEFT, Offset.UP_RIGHT);
    }

    private static MoveGenerator getRookMoveGenerator() {
        return new SlidingMoveGenerator(Offset.UP, Offset.DOWN, Offset.RIGHT, Offset.LEFT);
    }


    public static MoveGenerator getKnightMoveGenerator() {

        Offset[] offsets = new Offset[8];

        Offset.getKnightAttackOffsets().toArray(offsets);

        MoveGenerator moveGenerator = new NonSlidingMoveGenerator(true, offsets);

        return moveGenerator;
    }


    public static MoveGenerator getKingMoveGenerator() {

        CastlingMoveGenerator castlingMoveGenerator =
                new CastlingMoveGenerator(new BoardSeeker());

        NonSlidingMoveGenerator nonSlidingMoveGenerator =
                new NonSlidingMoveGenerator(true,
                        Offset.DOWN_LEFT, Offset.DOWN_RIGHT, Offset.UP_LEFT, Offset.UP_RIGHT,
                        Offset.UP, Offset.DOWN, Offset.RIGHT, Offset.LEFT);

        return (board, from, moveFactory) -> {
            List<Move> moves = new ArrayList<>();
            moves.addAll(castlingMoveGenerator.generateMoves(board, from, moveFactory));
            moves.addAll(nonSlidingMoveGenerator.generateMoves(board, from, moveFactory));
            return moves;
        };
    }
}
