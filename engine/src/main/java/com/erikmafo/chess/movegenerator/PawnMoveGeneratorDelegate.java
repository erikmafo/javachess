package com.erikmafo.chess.movegenerator;

import com.erikmafo.chess.board.Board;
import com.erikmafo.chess.board.BasicOffset;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.board.Offset;
import com.erikmafo.chess.move.Move;
import com.erikmafo.chess.move.MoveFactory;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by erikmafo on 20.11.16.
 */
public class PawnMoveGeneratorDelegate implements MoveGeneratorDelegate {

    private final MoveFactory moveFactory;
    private final Offset[] whiteAttackOffsets = {BasicOffset.UP_LEFT, BasicOffset.UP_RIGHT};
    private final Offset[] blackAttackOffsets = {BasicOffset.DOWN_LEFT, BasicOffset.DOWN_RIGHT};

    public PawnMoveGeneratorDelegate(MoveFactory moveFactory) {
        this.moveFactory = moveFactory;
    }

    @Override
    public List<Move> generateMoves(Board board, Square from) {

        List<Move> moves = new ArrayList<>();

        PieceColor pawnColor = board.getColorToMove();

        findPawnAttackMoves(board, pawnColor, from, moves);
        findPawnPromotionMoves(board, pawnColor, from, moves);
        findEnPassentMove(board, pawnColor,  from, moves);
        findQuietMoves(board, pawnColor, from, moves);

        return moves;
    }


    private Offset[] getAttackOffsets(PieceColor pawnColor) {
        return pawnColor.isWhite() ? whiteAttackOffsets : blackAttackOffsets;
    }


    private int getSeventRank(PieceColor color) {
        return color.isWhite() ? 7 : 1;
    }

    private int getSecondRank(PieceColor color) {
        return color.isWhite() ? 1 : 6;
    }

    private int getEnPassentRank(PieceColor color) {return color.isWhite() ? 4 : 3; }

    private Offset getUp(PieceColor pawnColor) {
        return pawnColor.isWhite() ? BasicOffset.UP : BasicOffset.DOWN;
    }


    private void findPawnAttackMoves(Board board, PieceColor pawnColor, Square from, List<Move> moves) {

        for (Offset offset : getAttackOffsets(pawnColor)) {

            Square target = from.next(offset);

            Piece piece = board.getNullablePiece(target);

            PieceColor opponent = pawnColor.opponent();

            if (piece != null && piece.getColor().equals(opponent)) {
                moves.add(moveFactory.newCaptureMove(board, from, target, piece));
            }
        }

    }

    private void findPawnPromotionMoves(Board board, PieceColor pawnColor,
                                        Square pieceLocation, List<Move> moves) {
        if (pieceLocation.getRank() != getSeventRank(pawnColor)) {
            return;
        }

        Square oneUp = pieceLocation.next(getUp(pawnColor));
        if (oneUp.isOnBoard() && !board.pieceAt(oneUp).isPresent()) {
            Piece pawn = board.pieceAt(pieceLocation).get();
            moves.add(moveFactory.newPawnPromotionMove(board, pieceLocation, oneUp, pawn,
                    new Piece(pawn.getColor(), PieceType.QUEEN)));
        }

    }


    private void findEnPassentMove(Board board, PieceColor color, Square from, List<Move> moves) {
        Optional<Square> enPassentTargetOptional = board.enPassentTarget();
        if (enPassentTargetOptional.isPresent() && from.getRank() == getEnPassentRank(color)) {
            Square target = enPassentTargetOptional.get();

            int fileDiff = target.getFile() - from.getFile();

            Optional<Piece> capturedOptional = Optional.empty();

            if (fileDiff == 1) {
                capturedOptional = board.pieceAt(from.next(BasicOffset.RIGHT));
            } else if (fileDiff == -1) {
                capturedOptional = board.pieceAt(from.next(BasicOffset.LEFT));
            }

            if (capturedOptional.isPresent()) {
                moves.add(moveFactory.newEnPassentMove(board, from, target, capturedOptional.get()));
            }
        }

    }



    private void findQuietMoves(Board board, PieceColor pawnColor, Square from, List<Move> moves) {

        // add single push
        boolean addedSinglePush = false;
        Square oneUp = from.next(getUp(pawnColor));
        if (oneUp.isOnBoard() && !board.isOccupied(oneUp)) {
            moves.add(moveFactory.newSinglePawnPushMove(board, from, oneUp));
            addedSinglePush = true;
        }

        // add double push
        Square twoUp = oneUp.next(getUp(pawnColor));
        if (from.getRank() == getSecondRank(pawnColor) && addedSinglePush && !board.isOccupied(twoUp)) {
            moves.add(moveFactory.newDoublePawnPushMove(board, from, twoUp));
        }
    }
}
