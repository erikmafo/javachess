package com.erikmafo.javachess.moves;

import com.erikmafo.javachess.boardrep.BoardCoordinate;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * Created by erikmafo on 15.11.16.
 */
public class IsMove extends TypeSafeDiagnosingMatcher<Move>{

    private final BoardCoordinate from;
    private final BoardCoordinate to;

    public IsMove(Class<?> expectedType, BoardCoordinate from, BoardCoordinate to) {
        super(expectedType);
        this.from = from;
        this.to = to;
    }

    public static Matcher<Move> isMove(Class<? extends  Move> expectedType,
                                       BoardCoordinate from, BoardCoordinate to) {
        return new IsMove(expectedType, from, to);

    }


    public static Matcher<Move> isMove(BoardCoordinate from, BoardCoordinate to) {
        return new IsMove(Move.class, from, to);

    }

    @Override
    protected boolean matchesSafely(Move move, Description description) {

        boolean match = true;

        if (!from.equals(move.getFrom()) || !to.equals(move.getTo())) {
            match = false;
            description
                    .appendText("was a move from ")
                    .appendValue(move.getFrom())
                    .appendText(" to ")
                    .appendValue(move.getTo());
        }

        return match;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("A move from " + from + " to " + to);
    }
}
