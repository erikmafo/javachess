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

    private static PawnMoveGeneratorDelegate getPawnMoveGenerator(MoveFactory moveFactory) {
        return new PawnMoveGeneratorDelegate(moveFactory);
    }

    private static MoveGeneratorDelegate getQueenMoveGenerator(MoveFactory moveFactory) {
        return new SlidingMoveGeneratorDelegate(
                moveFactory, BasicOffset.values());
    }

    private static MoveGeneratorDelegate getBishopMoveGenerator(MoveFactory moveFactory) {
        return new SlidingMoveGeneratorDelegate(
                moveFactory, BasicOffset.bishopValues());
    }

    private static MoveGeneratorDelegate getRookMoveGenerator(MoveFactory moveFactory) {
        return new SlidingMoveGeneratorDelegate(moveFactory, BasicOffset.rookValues());
    }


    private static MoveGeneratorDelegate getKnightMoveGenerator(MoveFactory moveFactory) {

        Offset[] offsets = KnightOffset.values();

        MoveGeneratorDelegate moveGeneratorDelegate = new NonSlidingMoveGeneratorDelegate(moveFactory, true, offsets);

        return moveGeneratorDelegate;
    }


    private static MoveGeneratorDelegate getKingMoveGenerator(MoveFactory moveFactory) {

        CastlingMoveGeneratorDelegate castlingMoveGenerator =
                new CastlingMoveGeneratorDelegate(moveFactory, new BoardSeeker());

        NonSlidingMoveGeneratorDelegate nonSlidingMoveGenerator = new NonSlidingMoveGeneratorDelegate(moveFactory, true, BasicOffset.values());
        return (board, from) -> {
            List<Move> moves = new ArrayList<>();
            moves.addAll(castlingMoveGenerator.generateMoves(board, from));
            moves.addAll(nonSlidingMoveGenerator.generateMoves(board, from));
            return moves;
        };
    }

}
