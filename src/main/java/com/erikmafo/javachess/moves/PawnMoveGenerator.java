package com.erikmafo.javachess.moves;

import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.Offset;
import com.erikmafo.javachess.boardrep.ReadableBoard;
import com.erikmafo.javachess.pieces.AttackTable;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.List;

/**
 * Created by erikmafo on 15.11.16.
 */
class PawnMoveGenerator extends NonSlidingMoveGenerator {

    private final Offset up;
    private final int secondRank;
    private final int fifthRank;
    private final int seventhRank;

    PawnMoveGenerator(PieceColor pieceColor) {
        super(pieceColor, PieceType.PAWN);
        up = pieceColor == PieceColor.WHITE ? Offset.UP : Offset.DOWN;
        secondRank = pieceColor == PieceColor.WHITE ?
                BoardCoordinate.A2.getRank() : BoardCoordinate.A7.getRank();
        fifthRank = pieceColor == PieceColor.WHITE ?
                BoardCoordinate.A5.getRank() : BoardCoordinate.A4.getRank();
        seventhRank = pieceColor == PieceColor.WHITE ?
                BoardCoordinate.A7.getRank() : BoardCoordinate.A2.getRank();
    }

    @Override
    public void findPossibleMoves(ReadableBoard board, BoardCoordinate pieceLocation, AttackTable attackTable, List<Move> moves) {
        super.findPossibleMoves(board, pieceLocation, attackTable, moves); // includes pawn promotion
        findEnPassentMove(board, pieceLocation, moves);
        findPawnPromotionMoves(board, pieceLocation, moves);
    }


    void findPawnPromotionMoves(ReadableBoard board, BoardCoordinate pieceLocation, List<Move> moves) {
        if (pieceLocation.getRank() != seventhRank) {
            return;
        }

        MoveGenerator queen = board.getMovingColor() == PieceColor.WHITE ?
                MoveGenerators.getWhiteQueen() : MoveGenerators.getBlackQueen();

        BoardCoordinate oneUp = pieceLocation.next(up);
        if (oneUp.isOnBoard() && !board.isOccupiedAt(oneUp)) {
            moves.add(Moves.createPawnPromotionMove(pieceLocation, oneUp, PieceType.QUEEN));
        }

    }


    void findEnPassentMove(ReadableBoard board, BoardCoordinate pieceLocation, List<Move> moves) {
        Move lastMove = board.getLastMove();
        if (lastMove == null) {
            return;
        }
        PieceType type = board.getPieceTypeAt(lastMove.getTo());
        boolean lastMoveIsDoublePawnPush =
                (2 == Math.abs(lastMove.getTo().getRank() - lastMove.getFrom().getRank())) && type == PieceType.PAWN;
        int fileDiff = Math.abs(lastMove.getTo().getFile() - pieceLocation.getFile());

        if (pieceLocation.getRank() == fifthRank && lastMoveIsDoublePawnPush && fileDiff == 1) {
            moves.add(Moves.createEnPassentMove(pieceLocation, lastMove.getTo().next(up)));
        }
    }


    @Override
    void findQuietMoves(ReadableBoard board, BoardCoordinate pieceLocation, AttackTable attackTable, List<Move> moves) {

        // add single push
        boolean addedSinglePush = false;
        BoardCoordinate oneUp = pieceLocation.next(up);
        if (oneUp.isOnBoard() && !board.isOccupiedAt(oneUp)) {
            moves.add(Moves.createQuietMove(pieceLocation, oneUp));
            addedSinglePush = true;
        }

        // add double push
        BoardCoordinate twoUp = oneUp.next(up);
        if (pieceLocation.getRank() == secondRank && addedSinglePush && !board.isOccupiedAt(twoUp)) {
            moves.add(Moves.createQuietMove(pieceLocation, twoUp));
        }
    }
}
