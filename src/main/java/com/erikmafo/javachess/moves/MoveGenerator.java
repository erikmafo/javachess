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


