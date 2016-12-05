package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.KnightOffset;
import com.erikmafo.javachess.board.Offset;
import com.erikmafo.javachess.board.BasicOffset;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikmafo on 03.12.16.
 */
public class MoveGeneratorFactory {


    public MoveGenerator newInstance(MoveGenerationStrategy strategy, MoveFactory moveFactory) {
        return newPseudoLegalMoveGenerator(moveFactory);
    }



    private static MoveGenerator newPseudoLegalMoveGenerator(MoveFactory moveFactory) {

        MoveGenerator moveGenerator = new CompositeMoveGenerator.Builder()
                .setMoveGenerator(PieceType.PAWN, getPawnMoveGenerator(moveFactory))
                .setMoveGenerator(PieceType.BISHOP, getBishopMoveGenerator(moveFactory))
                .setMoveGenerator(PieceType.ROOK, getRookMoveGenerator(moveFactory))
                .setMoveGenerator(PieceType.KNIGHT, getKnightMoveGenerator(moveFactory))
                .setMoveGenerator(PieceType.QUEEN, getQueenMoveGenerator(moveFactory))
                .setMoveGenerator(PieceType.KING, getKingMoveGenerator(moveFactory))
                .build();


        return moveGenerator;
    }

    private static PawnMoveGenerator getPawnMoveGenerator(MoveFactory moveFactory) {
        return new PawnMoveGenerator(moveFactory);
    }

    private static MoveGenerator getQueenMoveGenerator(MoveFactory moveFactory) {
        return new SlidingMoveGenerator(
                moveFactory, BasicOffset.values());
    }

    private static MoveGenerator getBishopMoveGenerator(MoveFactory moveFactory) {
        return new SlidingMoveGenerator(
                moveFactory, BasicOffset.bishopValues());
    }

    private static MoveGenerator getRookMoveGenerator(MoveFactory moveFactory) {
        return new SlidingMoveGenerator(moveFactory, BasicOffset.rookValues());
    }


    private static MoveGenerator getKnightMoveGenerator(MoveFactory moveFactory) {

        Offset[] offsets = KnightOffset.values();

        MoveGenerator moveGenerator = new NonSlidingMoveGenerator(moveFactory, true, offsets);

        return moveGenerator;
    }


    private static MoveGenerator getKingMoveGenerator(MoveFactory moveFactory) {

        CastlingMoveGenerator castlingMoveGenerator =
                new CastlingMoveGenerator(moveFactory, new BoardSeeker());

        NonSlidingMoveGenerator nonSlidingMoveGenerator = new NonSlidingMoveGenerator(moveFactory, true, BasicOffset.values());
        return (board, from) -> {
            List<Move> moves = new ArrayList<>();
            moves.addAll(castlingMoveGenerator.generateMoves(board, from));
            moves.addAll(nonSlidingMoveGenerator.generateMoves(board, from));
            return moves;
        };
    }

}
