package com.erikmafo.chess.gui.view;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

/**
 * Created by erikmafo on 27.12.16.
 */
public class ChessboardEvent extends Event {

    public static final EventType<ChessboardEvent> ANY = new EventType<>(EventType.ROOT);

    public static final EventType<ChessboardEvent> SQUARE_PRESSED = new EventType<>(ANY, "SQUARE_PRESSED");
    public static final EventType<ChessboardEvent> PIECE_PRESSED = new EventType<>(ANY, "PIECE_PRESSED");

    public static final EventType<ChessboardEvent> SQUARE_CLICKED = new EventType<>(ANY, "SQUARE_CLICKED");
    public static final EventType<ChessboardEvent> PIECE_CLICKED = new EventType<>(ANY, "PIECE_CLICKED");

    public static final EventType<ChessboardEvent> PIECE_RELEASED = new EventType<>(ANY, "PIECE_RELEASED");


    private final int file;
    private final int rank;

    public ChessboardEvent(@NamedArg("eventType") EventType<? extends Event> eventType, int file, int rank) {
        super(eventType);
        this.file = file;
        this.rank = rank;
    }

    public ChessboardEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<? extends Event> eventType, int file, int rank) {
        super(source, target, eventType);
        this.file = file;
        this.rank = rank;
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }
}
