package com.erikmafo.javachess.search;

import com.erikmafo.javachess.moves.Move;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by erikfolstad on 27.06.15.
 */
public class MoveComparator implements Comparator<Move> {

    @Override
    public int compare(Move o1, Move o2) {
        return Integer.compare(o1.getScore(), o2.getScore());
    }

    @Override
    public Comparator<Move> reversed() {
        return Collections.reverseOrder(this);
    }
}
