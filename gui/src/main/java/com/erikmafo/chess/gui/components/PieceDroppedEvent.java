package com.erikmafo.chess.gui.components;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Created by erikmafo on 31.12.16.
 */
public class PieceDroppedEvent extends ChessboardEvent {


    public static final EventType<PieceDroppedEvent> ANY = new EventType<>(ChessboardEvent.ANY, "PieceDropped");


    private boolean pieceDropAccepted;
    private final BoardLocation initialLocation;

    public PieceDroppedEvent(@NamedArg("eventType") EventType<? extends Event> eventType, BoardLocation initialLocation, BoardLocation boardLocation) {
        super(eventType, boardLocation);
        this.initialLocation = initialLocation;
    }

    public PieceDroppedEvent(BoardLocation initialLocation, BoardLocation dropLocation) {
        this(ANY, initialLocation, dropLocation);
    }

    public boolean isPieceDropAccepted() {
        return pieceDropAccepted;
    }

    public void setPieceDropAccepted(boolean pieceDropAccepted) {
        this.pieceDropAccepted = pieceDropAccepted;
    }


    public BoardLocation getInitialLocation() {
        return initialLocation;
    }
}
