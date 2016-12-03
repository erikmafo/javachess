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
 * Created by erikmafo on 19.11.16.
 */
public class NonSlidingMoveGenerator implements MoveGenerator {

    private final MoveFactory moveFactory;
    private final boolean includeQuietMoves;
    private final Offset[] attackOffsets;



    public NonSlidingMoveGenerator(MoveFactory moveFactory, boolean includeQuietMoves, Offset... offsets) {
        this.moveFactory = moveFactory;
        this.includeQuietMoves = includeQuietMoves;
        this.attackOffsets = offsets;
    }

    @Override
    public List<Move> generateMoves(Board board, BoardCoordinate from) {
        return getMoves(moveFactory, board, board.getColorToMove().getOpposite(), from, includeQuietMoves);
    }

    private List<Move> getMoves(MoveFactory moveFactory, Board board, PieceColor opponent, BoardCoordinate from, boolean includeQuietMoves) {
        List<Move> moves = new ArrayList<>();

        for (Offset offset : attackOffsets) {
            BoardCoordinate target = from.next(offset);
            if (target.isOnBoard()) {
                Optional<Piece> pieceOptional = board.pieceAt(target);
                if (!pieceOptional.isPresent()) {
                    if (includeQuietMoves) {
                        moves.add(moveFactory.newQuietMove(from, target));
                    }
                } else if (pieceOptional.filter(piece -> piece.getColor().equals(opponent)).isPresent()) {
                    moves.add(moveFactory.newCaptureMove(from, target, pieceOptional.get()));
                }
            } else {
                break;
            }
        }
        return moves;
    }


}
