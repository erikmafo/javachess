package com.erikmafo.javachess.board;

import com.erikmafo.javachess.movegenerator.MoveGeneratorFactory;
import com.erikmafo.javachess.pieces.Piece;
import com.erikmafo.javachess.pieces.PieceColor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by erikf on 22.05.2016.
 */
public class BoardBuilder {


    private Map<BoardCoordinate, Piece> pieceEntries = new HashMap<>();
    private BoardCoordinate enPassentTarget;
    private PieceColor activeColor = PieceColor.WHITE;
    private int halfMoveCount = 0;
    private final Map<PieceColor, CastlingRight> castlingRights = new HashMap<>();

    public BoardBuilder put(BoardCoordinate square, Piece piece) {
        pieceEntries.put(square, piece);
        return this;
    }

    public BoardBuilder setEnPassentTarget(BoardCoordinate enPassentTarget) {
        this.enPassentTarget = enPassentTarget;
        return this;
    }

    public BoardBuilder setActiveColor(PieceColor activeColor) {
        this.activeColor = activeColor;
        return this;
    }

    public BoardBuilder setHalfMoveCount(int halfMoveCount) {
        this.halfMoveCount = halfMoveCount;
        return this;
    }


    public Board build() {

        BoardImpl board = new BoardImpl(new MoveGeneratorFactory(), activeColor, castlingRights);

        for (BoardCoordinate square : pieceEntries.keySet()) {
            board.put(square, pieceEntries.get(square));
        }

        if (enPassentTarget != null) {
            board.setEnPassentTarget(enPassentTarget);
        }


        return board;
    }


    public void setCastlingRights(PieceColor color, CastlingRight castlingRight) {

        castlingRights.put(color, castlingRight);

    }

}


