package com.erikmafo.chess2.utils;

import com.erikmafo.chess.board.CastlingRight;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;

import java.util.Map;

/**
 * Created by erikmafo on 20.11.17.
 */
public class FenParseResult {

    private final Map<Square, Piece> piecePlacement;
    private final Map<PieceColor, CastlingRight> castlingRighs;
    private final PieceColor colorToMove;
    private final Square enPassentTarget;

    public FenParseResult(Map<Square, Piece> piecePlacement, Map<PieceColor, CastlingRight> castlingRighs, PieceColor colorToMove, Square enPassentTarget) {
        this.piecePlacement = piecePlacement;
        this.castlingRighs = castlingRighs;
        this.colorToMove = colorToMove;
        this.enPassentTarget = enPassentTarget;
    }

    public Map<Square, Piece> getPiecePlacement() {
        return piecePlacement;
    }

    public Map<PieceColor, CastlingRight> getCastlingRighs() {
        return castlingRighs;
    }

    public Square getEnPassentTarget() {
        return enPassentTarget;
    }

    public PieceColor getColorToMove() {
        return colorToMove;
    }
}
