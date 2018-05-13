package com.erikmafo.chess2.movegeneration;


import com.erikmafo.chess.board.CastlingRight;
import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.piece.PieceType;

import java.util.List;

public interface Board {

    void play(Move move);

    void undoLastMove();

    PieceColor colorToMove();

    Piece at(Square square);

    boolean isCheck();

    boolean isChecked(PieceColor color);

    boolean isStalemate();

    CastlingRight getCastlingRigth(PieceColor color);

    long hash();

    List<Move> generateMoves();

    int count(PieceColor color, PieceType pieceType);

    Board deepCopy();
}
