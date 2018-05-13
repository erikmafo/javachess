package com.erikmafo.chess.gui.components;

import com.erikmafo.chess.gui.model.BoardLocation;
import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.event.EventType;

/**
 * Created by erikmafo on 31.12.16.
 */
public class PieceDroppedEvent extends Event {


    public static final EventType<PieceDroppedEvent> ANY = new EventType<>(EventType.ROOT, "PieceDropped");


    private boolean pieceDropAccepted;
    private final BoardLocation initialLocation;
    private final BoardLocation dropLocation;

    public PieceDroppedEvent(@NamedArg("eventType") EventType<? extends Event> eventType, BoardLocation initialLocation, BoardLocation dropLocation) {
        super(eventType);
        this.initialLocation = initialLocation;
        this.dropLocation = dropLocation;
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

    public BoardLocation getDropLocation() { return dropLocation; }
}
