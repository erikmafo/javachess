package com.erikmafo.javachess.moves;

import com.erikmafo.javachess.boardrep.Offset;
import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.ReadableBoard;
import com.erikmafo.javachess.pieces.AttackTable;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;

import java.util.List;
import java.util.Set;


public class MoveGenerator {
    final PieceColor pieceColor;
    private final Set<Offset> attackOffsets;

    MoveGenerator() {
        pieceColor = null;
        attackOffsets = null;
    }

    MoveGenerator(PieceColor pieceColor, PieceType pieceType) {
        this.pieceColor = pieceColor;
        attackOffsets = Offset.getOffsets(pieceColor, pieceType);
    }

    public Set<Offset> getAttackOffsets() {
        return attackOffsets;
    }

    public void findAttackSquares(ReadableBoard readableBoard, BoardCoordinate pieceLocation, AttackTable attackTable) {
        attackTable.clear();
    }

    public void findPossibleMoves(ReadableBoard board, BoardCoordinate pieceLocation, AttackTable attackTable, List<Move> moves) {
        findQuietMoves(board, pieceLocation, attackTable, moves);
        findCaptureMoves(board, pieceLocation, attackTable, moves);
    }

    void findQuietMoves(ReadableBoard board, BoardCoordinate pieceLocation, AttackTable attackTable, List<Move> moves) {
        for (BoardCoordinate target : attackTable.getEmptyTargets()) {
            moves.add(Moves.createQuietMove(pieceLocation, target));
        }
    }

    void findCaptureMoves(ReadableBoard board, BoardCoordinate pieceLocation, AttackTable attackTable, List<Move> moves) {
        for (BoardCoordinate target : attackTable.getOccupiedTargets()) {
            if (board.getPieceColorAt(target) != board.getMovingColor()) {
                moves.add(Moves.createCaptureMove(pieceLocation, target));
            }
        }
    }
}


class SlidingMoveGenerator extends MoveGenerator {

    SlidingMoveGenerator(PieceColor pieceColor, PieceType pieceType) {
        super(pieceColor, pieceType);
    }

    @Override
    public void findAttackSquares(ReadableBoard readableBoard, BoardCoordinate pieceLocation, AttackTable attackTable) {
        super.findAttackSquares(readableBoard, pieceLocation, attackTable);
        for (Offset offset : getAttackOffsets()) {
            BoardCoordinate target = pieceLocation.getNext(offset);
            while (target.isOnBoard()) {
                if (!readableBoard.isOccupiedAt(target)) {
                    attackTable.addEmptyTarget(target);
                    target = target.getNext(offset);
                    continue;
                } else if (readableBoard.getPieceColorAt(target) != pieceColor) {
                    attackTable.addOccupiedTarget(target);
                }
                break;
            }
        }
    }
}


class NonSlidingMoveGenerator extends MoveGenerator {
    NonSlidingMoveGenerator(PieceColor pieceColor, PieceType pieceType) {
        super(pieceColor, pieceType);
    }

    @Override
    public void findAttackSquares(ReadableBoard readableBoard, BoardCoordinate pieceLocation, AttackTable attackTable) {
        super.findAttackSquares(readableBoard, pieceLocation, attackTable);
        for (Offset offset : getAttackOffsets()) {
            BoardCoordinate target = pieceLocation.getNext(offset);
            if (!target.isOnBoard()) {
                continue;
            }
            if (!readableBoard.isOccupiedAt(target)) {
                attackTable.addEmptyTarget(target);
            } else if (readableBoard.getPieceColorAt(target) != pieceColor) {
                attackTable.addOccupiedTarget(target);
            }
        }
    }
}

class KingMoveGenerator extends NonSlidingMoveGenerator {

    private BoardCoordinate[] kingSideCastlingSquares;
    private BoardCoordinate[] queenSideCastlingSquares;

    private BoardCoordinate kingSideCastlingTarget;
    private BoardCoordinate queenSideCastlingTarget;

    private BoardCoordinate initialKingSideRookCoordinate;
    private BoardCoordinate initialQueenSideRookCoordinate;

    KingMoveGenerator(PieceColor pieceColor) {
        super(pieceColor, PieceType.KING);
        if (pieceColor.isWhite()) {
            kingSideCastlingSquares = new BoardCoordinate[]{BoardCoordinate.F1, BoardCoordinate.G1};
            queenSideCastlingSquares = new BoardCoordinate[]{BoardCoordinate.D1, BoardCoordinate.C1,
                    BoardCoordinate.B1};
            kingSideCastlingTarget = BoardCoordinate.G1;
            queenSideCastlingTarget = BoardCoordinate.C1;

            initialKingSideRookCoordinate = BoardCoordinate.H1;
            initialQueenSideRookCoordinate = BoardCoordinate.A1;

        } else {
            kingSideCastlingSquares = new BoardCoordinate[]{BoardCoordinate.F8, BoardCoordinate.G8};
            queenSideCastlingSquares = new BoardCoordinate[]{BoardCoordinate.D8, BoardCoordinate.C8,
                    BoardCoordinate.B8};
            kingSideCastlingTarget = BoardCoordinate.G8;
            queenSideCastlingTarget = BoardCoordinate.C8;

            initialKingSideRookCoordinate = BoardCoordinate.H8;
            initialQueenSideRookCoordinate = BoardCoordinate.A8;
        }
    }

    @Override
    public void findPossibleMoves(ReadableBoard board, BoardCoordinate pieceLocation,
                                  AttackTable attackTable, List<Move> moves) {
        super.findPossibleMoves(board, pieceLocation, attackTable, moves);

        // check for castling moves

        if (isKingSideCastlingLegal(board)) {
            moves.add(Moves.createCastlingMove(pieceLocation, kingSideCastlingTarget,
                    initialKingSideRookCoordinate, kingSideCastlingTarget.getNext(Offset.LEFT)));
        }

        if (isQueenSideCastlingLegal(board)) {
            moves.add(Moves.createCastlingMove(pieceLocation, queenSideCastlingTarget,
                    initialQueenSideRookCoordinate, queenSideCastlingTarget.getNext(Offset.RIGHT)));
        }

    }


    private boolean isKingSideCastlingLegal(ReadableBoard board) {
        if (board.isChecked(pieceColor) || !board.isKingSideCastlingPossible(pieceColor)) {
            return false;
        }

        for (BoardCoordinate coordinate : kingSideCastlingSquares) {
            if (board.isOccupiedAt(coordinate) || board.isAttacked(pieceColor, coordinate)) {
                return false;
            }
        }

        return true;
    }


    private boolean isQueenSideCastlingLegal(ReadableBoard board) {
        if (board.isChecked(pieceColor) || !board.isQueenSideCastlingPossible(pieceColor)) {
            return false;
        }

        for (BoardCoordinate coordinate : queenSideCastlingSquares) {
            if (board.isOccupiedAt(coordinate)) {
                return false;
            }
        }

        return !(board.isAttacked(pieceColor, queenSideCastlingSquares[0]) ||
                board.isAttacked(pieceColor, queenSideCastlingSquares[1]));

    }



}


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

        BoardCoordinate oneUp = pieceLocation.getNext(up);
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
            moves.add(Moves.createEnPassentMove(pieceLocation, lastMove.getTo().getNext(up)));
        }
    }


    @Override
    void findQuietMoves(ReadableBoard board, BoardCoordinate pieceLocation, AttackTable attackTable, List<Move> moves) {

        // add single push
        boolean addedSinglePush = false;
        BoardCoordinate oneUp = pieceLocation.getNext(up);
        if (oneUp.isOnBoard() && !board.isOccupiedAt(oneUp)) {
            moves.add(Moves.createQuietMove(pieceLocation, oneUp));
            addedSinglePush = true;
        }

        // add double push
        BoardCoordinate twoUp = oneUp.getNext(up);
        if (pieceLocation.getRank() == secondRank && addedSinglePush && !board.isOccupiedAt(twoUp)) {
            moves.add(Moves.createQuietMove(pieceLocation, twoUp));
        }
    }
}
