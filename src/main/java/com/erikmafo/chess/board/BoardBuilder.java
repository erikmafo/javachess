package com.erikmafo.chess.board;

import com.erikmafo.chess.movegenerator.MoveGeneratorFactory;
import com.erikmafo.chess.pieces.Piece;
import com.erikmafo.chess.pieces.PieceColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by erikf on 22.05.2016.
 */
public class BoardBuilder {


    private Map<Square, Piece> pieceEntries = new HashMap<>();
    private Square enPassentTarget;
    private PieceColor activeColor = PieceColor.WHITE;
    private int halfMoveCount = 0;
    private final Map<PieceColor, CastlingRight> castlingRights = new HashMap<>();

    public BoardBuilder put(Square square, Piece piece) {
        pieceEntries.put(square, piece);
        return this;
    }

    public BoardBuilder setEnPassentTarget(Square enPassentTarget) {
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

        BoardImpl board = new BoardImpl(new MoveGeneratorFactory(), new ZobristCalculator(new Random(1)), activeColor, castlingRights);

        for (Square square : pieceEntries.keySet()) {
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


