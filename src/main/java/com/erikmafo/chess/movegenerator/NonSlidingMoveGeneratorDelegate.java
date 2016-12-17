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
 * Created by erikmafo on 19.11.16.
 */
public class NonSlidingMoveGeneratorDelegate implements MoveGeneratorDelegate {

    private final MoveFactory moveFactory;
    private final boolean includeQuietMoves;
    private final Offset[] attackOffsets;



    public NonSlidingMoveGeneratorDelegate(MoveFactory moveFactory, boolean includeQuietMoves, Offset... offsets) {
        this.moveFactory = moveFactory;
        this.includeQuietMoves = includeQuietMoves;
        this.attackOffsets = offsets;
    }

    @Override
    public List<Move> generateMoves(Board board, Square from) {
        return getMoves(board, board.getColorToMove().getOpposite(), from, includeQuietMoves);
    }

    private List<Move> getMoves(Board board, PieceColor opponent, Square from, boolean includeQuietMoves) {
        List<Move> moves = new ArrayList<>();

        for (Offset offset : attackOffsets) {
            Square target = from.next(offset);
            if (target.isOnBoard()) {
                Optional<Piece> pieceOptional = board.pieceAt(target);
                if (!pieceOptional.isPresent()) {
                    if (includeQuietMoves) {
                        moves.add(moveFactory.newQuietMove(board, from, target));
                    }
                } else if (pieceOptional.filter(piece -> piece.getColor().equals(opponent)).isPresent()) {
                    moves.add(moveFactory.newCaptureMove(board, from, target, pieceOptional.get()));
                }
            }
        }
        return moves;
    }


}
