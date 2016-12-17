package com.erikmafo.chess.movegenerator;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.board.Offset;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.move.MoveFactory;
import com.erikmafo.chess.pieces.Piece;
import com.erikmafo.chess.pieces.PieceColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by erikmafo on 20.11.16.
 */
public class SlidingMoveGeneratorDelegate implements MoveGeneratorDelegate {


    private final MoveFactory moveFactory;
    private final Offset[] attackOffsets;

    public SlidingMoveGeneratorDelegate(MoveFactory moveFactory, Offset... attackOffsets) {
        this.moveFactory = moveFactory;
        this.attackOffsets = attackOffsets;
    }


    @Override
    public List<Move> generateMoves(Board board, Square from) {

        List<Move> moves = new ArrayList<>();

        PieceColor opponent = board.getColorToMove().getOpposite();

        for (Offset offset : attackOffsets) {
            Square target = from.next(offset);
            while (target.isOnBoard()) {
                Optional<Piece> targetPiece = board.pieceAt(target);
                if (targetPiece.isPresent()) {
                    if (targetPiece.get().getColor().equals(opponent)) {
                        moves.add(moveFactory.newCaptureMove(board, from, target, targetPiece.get()));
                    }
                    break;
                } else {
                    moves.add(moveFactory.newQuietMove(board, from, target));
                    target = target.next(offset);
                }
            }
        }

        return moves;

    }

}
