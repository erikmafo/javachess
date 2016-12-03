package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.board.Offset;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by erikmafo on 20.11.16.
 */
public class SlidingMoveGenerator implements MoveGenerator {


    private final MoveFactory moveFactory;
    private final Offset[] attackOffsets;

    public SlidingMoveGenerator(MoveFactory moveFactory, Offset... attackOffsets) {
        this.moveFactory = moveFactory;
        this.attackOffsets = attackOffsets;
    }


    @Override
    public List<Move> generateMoves(Board board, BoardCoordinate from) {

        List<Move> moves = new ArrayList<>();

        PieceColor opponent = board.getColorToMove().getOpposite();

        for (Offset offset : attackOffsets) {
            BoardCoordinate target = from.next(offset);
            while (target.isOnBoard()) {
                Optional<Piece> targetPiece = board.pieceAt(target);
                if (targetPiece.isPresent()) {
                    if (targetPiece.get().getColor().equals(opponent)) {
                        moves.add(moveFactory.newCaptureMove(from, target, targetPiece.get()));
                    }
                    break;
                } else {
                    moves.add(moveFactory.newQuietMove(from, target));
                    target = target.next(offset);
                }
            }
        }

        return moves;

    }

}
