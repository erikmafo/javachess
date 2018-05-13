package com.erikmafo.chess2.movegeneration;

import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;
import org.jetbrains.annotations.NotNull;

public class Move implements Comparable<Move> {

    public enum Kind {
        QUIET,
        DOUBLE_PAWN_PUSH,
        CAPTURE,
        EN_PASSENT,
        KNIGHT_PROMOTION,
        BISHOP_PROMOTION,
        ROOK_PROMOTION,
        QUEEN_PROMOTION,
        KING_SIDE_CASTLE,
        QUEEN_SIDE_CASTLE
    }

    private final PieceColor movingColor;
    private final PieceType movingPiece;
    private final Kind kind;
    private final Square from;
    private final Square to;
    private final PieceType capturedPieceType;

    public Move( PieceColor movingColor, PieceType movingPiece, Kind kind, Square from, Square to, PieceType capturedPieceType) {
        this.from = from;
        this.to = to;
        this.kind = kind;
        this.movingPiece = movingPiece;
        this.movingColor = movingColor;
        this.capturedPieceType = capturedPieceType;
    }

    public Move(Piece movingPiece, Kind kind, Square from, Square to) {
        this(movingPiece.getColor(), movingPiece.getType(), kind, from, to, null);
    }

    public Move(PieceColor movingColor, PieceType movingPiece, Kind kind, Square from, Square to) {
        this(movingColor, movingPiece, kind, from, to, null);
    }

    public Square from() {
        return from;
    }

    public Square to() {
        return to;
    }

    public Kind kind() {
        return kind;
    }

    public PieceType getMovingPieceType() {
        return movingPiece;
    }

    public PieceColor getMovingColor() {
        return movingColor;
    }

    public PieceType getCapturedPieceType() {
        return capturedPieceType;
    }

    @Override
    public int compareTo(@NotNull Move o) {

        int res = 0;

        if ((res = movingColor.compareTo(o.movingColor)) != 0) {
            return res;
        }

        if ((res = kind.compareTo(o.kind)) != 0) {
            return res;
        }

        if (capturedPieceType != null) {
            if (o.capturedPieceType == null) {
                return 1;
            }

            res = capturedPieceType.compareTo(o.capturedPieceType);

            if (res != 0) {
                return res;
            } else if ((res = -movingPiece.compareTo(o.movingPiece)) != 0) {
                return res;
            }
        }

        if ((res = -movingPiece.compareTo(o.movingPiece)) != 0) {
            return res;
        }

        if ((res = from.compareTo(o.from)) != 0) {
            return res;
        }

        if ((res = to.compareTo(o.to)) != 0) {
            return res;
        }

        return 0;
    }

    @Override
    public String toString() {

        String moveAsString;

        if (kind().equals(Kind.KING_SIDE_CASTLE)) {
            moveAsString = "O-O";
        } else if (kind().equals(Kind.QUEEN_SIDE_CASTLE)) {
            moveAsString = "O-O-O";
        } else {
            moveAsString = (from.toString() + "-" + to.toString()).toLowerCase();
        }

        return moveAsString;
    }
}
