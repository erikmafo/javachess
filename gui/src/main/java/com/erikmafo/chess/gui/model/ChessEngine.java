package com.erikmafo.chess.gui.model;

import com.erikmafo.chess.board.Square;
import com.erikmafo.chess.piece.Piece;
import com.erikmafo.chess.piece.PieceColor;
import com.erikmafo.chess.utils.parser.FenParseException;
import javafx.concurrent.Task;

import java.util.List;
import java.util.Map;

/**
 * Created by erikmafo on 03.12.17.
 */
public interface ChessEngine {

    Map<Square, Piece> getSquarePieceMap();

    PieceColor getColorToMove();

    void setPosition(String fen, ChessMove... moves) throws FenParseException;

    void setPosition(String fen, List<ChessMove> moves) throws FenParseException;

    boolean isLegal(ChessMove chessMove);

    Task<EngineSearchResult> go();

    void shutdown();
}
