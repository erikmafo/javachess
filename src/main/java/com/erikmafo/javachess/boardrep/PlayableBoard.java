package com.erikmafo.javachess.boardrep;

import com.erikmafo.javachess.moves.Move;

import java.util.List;


public interface PlayableBoard extends ReadableBoard {

    void undoLast();

    void play(Move move);

    void fillWithPossibleMoves(List<Move> moveSet);

}
