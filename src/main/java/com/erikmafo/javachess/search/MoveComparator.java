package com.erikmafo.javachess.search;

import com.erikmafo.javachess.move.Move;

import java.util.Comparator;

/**
 * Created by erikmafo on 04.12.16.
 */
public class MoveComparator implements Comparator<Move> {

    @Override
    public int compare(Move move1, Move move2) {
        return Integer.compare(move1.getRank(), move2.getRank());
    }
}
