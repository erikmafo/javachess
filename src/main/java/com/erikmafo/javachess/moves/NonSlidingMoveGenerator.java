package com.erikmafo.javachess.moves;

import com.erikmafo.javachess.boardrep.BoardCoordinate;
import com.erikmafo.javachess.boardrep.Offset;
import com.erikmafo.javachess.boardrep.ReadableBoard;
import com.erikmafo.javachess.pieces.AttackTable;
import com.erikmafo.javachess.pieces.PieceColor;
import com.erikmafo.javachess.pieces.PieceType;

/**
 * Created by erikmafo on 15.11.16.
 */
class NonSlidingMoveGenerator extends MoveGenerator {
    NonSlidingMoveGenerator(PieceColor pieceColor, PieceType pieceType) {
        super(pieceColor, pieceType);
    }

    @Override
    public void findAttackSquares(ReadableBoard readableBoard, BoardCoordinate pieceLocation, AttackTable attackTable) {
        super.findAttackSquares(readableBoard, pieceLocation, attackTable);
        for (Offset offset : getAttackOffsets()) {
            BoardCoordinate target = pieceLocation.next(offset);
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
