package com.erikmafo.javachess.board;


/**
 * Created by erikf on 16.07.2016.
 */
public class FenParseException extends Exception {

    public FenParseException() {
    }

    public FenParseException(String message) {
        super(message);
    }

    public FenParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FenParseException(Throwable cause) {
        super(cause);
    }
}

