package com.erikmafo.chess.gui.model;

/**
 * Created by erikmafo on 07.01.17.
 */
public class EngineSearchResult {


    private final ChessMove bestMove;


    public EngineSearchResult(ChessMove bestMove) {
        this.bestMove = bestMove;
    }

    public ChessMove getBestMove() {
        return bestMove;
    }
}
