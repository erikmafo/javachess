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
class SlidingMoveGenerator extends MoveGenerator {

    SlidingMoveGenerator(PieceColor pieceColor, PieceType pieceType) {
        super(pieceColor, pieceType);
    }

    @Override
    public void findAttackSquares(ReadableBoard readableBoard, BoardCoordinate pieceLocation, AttackTable attackTable) {
        super.findAttackSquares(readableBoard, pieceLocation, attackTable);
        for (Offset offset : getAttackOffsets()) {
            BoardCoordinate target = pieceLocation.next(offset);
            while (target.isOnBoard()) {
                if (!readableBoard.isOccupiedAt(target)) {
                    attackTable.addEmptyTarget(target);
                    target = target.next(offset);
                    continue;
                } else if (readableBoard.getPieceColorAt(target) != pieceColor) {
                    attackTable.addOccupiedTarget(target);
                }
                break;
            }
        }
    }
}
