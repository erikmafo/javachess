package com.erikmafo.chess.gui.components;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Created by erikmafo on 01.01.17.
 */
public class GameBoardEvent extends Event {

    public final static EventType<GameBoardEvent> ANY = new EventType<>(Event.ANY, "ANY");
    public final static EventType<GameBoardEvent> SQUARE_CLICKED = new EventType<>(ANY, "SQUARE_CLICKED");


    private final int file;
    private final int rank;


    public GameBoardEvent(@NamedArg("eventType") EventType<? extends Event> eventType, int file, int rank) {
        super(eventType);
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
