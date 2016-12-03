package com.erikmafo.javachess.movegenerator;

import com.erikmafo.javachess.board.Board;
import com.erikmafo.javachess.board.BoardCoordinate;
import com.erikmafo.javachess.board.Offset;
import com.erikmafo.javachess.move.Move;
import com.erikmafo.javachess.move.MoveFactory;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by erikmafo on 20.11.16.
 */
public class PawnMoveGenerator implements MoveGenerator {

    private final MoveFactory moveFactory;
    private final Offset[] whiteAttackOffsets = {Offset.UP_LEFT, Offset.UP_RIGHT};
    private final Offset[] blackAttackOffsets = {Offset.DOWN_LEFT, Offset.DOWN_RIGHT};

    public PawnMoveGenerator(MoveFactory moveFactory) {
        this.moveFactory = moveFactory;
    }

    @Override
    public List<Move> generateMoves(Board board, BoardCoordinate from) {

        List<Move> moves = new ArrayList<>();

        PieceColor pawnColor = board.getColorToMove();

        findPawnAttackMoves(moveFactory, board, pawnColor, from, moves);
        findPawnPromotionMoves(moveFactory, board, pawnColor, from, moves);
        findEnPassentMove(moveFactory, board, from, moves);
        findQuietMoves(moveFactory, board, pawnColor, from, moves);

        return moves;
    }


    private Offset[] getAttackOffsets(PieceColor pawnColor) {
        return pawnColor.isWhite() ? whiteAttackOffsets : blackAttackOffsets;
    }




    private int getSeventRank(PieceColor color) {
        return color.isWhite() ? 7 : 1;
    }

    private int getSecondRank(PieceColor color) {
        return color.isWhite() ? 1 : 7;
    }

    private Offset getUp(PieceColor pawnColor) {
        return pawnColor.isWhite() ? Offset.UP : Offset.DOWN;
    }


    private void findPawnAttackMoves(MoveFactory moveFactory, Board board, PieceColor pawnColor, BoardCoordinate from, List<Move> moves) {

        for (Offset offset : getAttackOffsets(pawnColor)) {

            BoardCoordinate target = from.next(offset);

            Optional<Piece> piece = board.pieceAt(target);

            PieceColor opponent = pawnColor.getOpposite();

            if (piece.filter(p -> p.getColor().equals(opponent)).isPresent()) {
                moves.add(moveFactory.newCaptureMove(from, target, piece.get()));
            }
        }

    }

    private void findPawnPromotionMoves(MoveFactory moveFactory, Board board, PieceColor pawnColor,
                                BoardCoordinate pieceLocation, List<Move> moves) {
        if (pieceLocation.getRank() != getSeventRank(pawnColor)) {
            return;
        }

        BoardCoordinate oneUp = pieceLocation.next(getUp(pawnColor));
        if (oneUp.isOnBoard() && !board.pieceAt(oneUp).isPresent()) {
            Piece pawn = board.pieceAt(pieceLocation).get();
            moves.add(moveFactory.newPawnPromotionMove(pieceLocation, oneUp, pawn,
                    new Piece(pawn.getColor(), PieceType.QUEEN)));
        }

    }


    private void findEnPassentMove(MoveFactory moveFactory, Board board, BoardCoordinate from, List<Move> moves) {
        Optional<BoardCoordinate> enPassentTargetOptional = board.enPassentTarget();
        if (enPassentTargetOptional.isPresent()) {
            BoardCoordinate target = enPassentTargetOptional.get();
            Offset offset = target.getFile() > from.getFile() ? Offset.RIGHT : Offset.LEFT;
            Piece captured = board.pieceAt(from.next(offset)).get();
            moves.add(moveFactory.newEnPassentMove(from, target, captured));
        }

    }



    private void findQuietMoves(MoveFactory moveFactory, Board board, PieceColor pawnColor, BoardCoordinate from, List<Move> moves) {

        // add single push
        boolean addedSinglePush = false;
        BoardCoordinate oneUp = from.next(getUp(pawnColor));
        if (oneUp.isOnBoard() && !board.isOccupied(oneUp)) {
            moves.add(moveFactory.newSinglePawnPushMove(from, oneUp));
            addedSinglePush = true;
        }

        // add double push
        BoardCoordinate twoUp = oneUp.next(getUp(pawnColor));
        if (from.getRank() == getSecondRank(pawnColor) && addedSinglePush && !board.isOccupied(twoUp)) {
            moves.add(moveFactory.newDoublePawnPushMove(from, twoUp));
        }
    }
}
