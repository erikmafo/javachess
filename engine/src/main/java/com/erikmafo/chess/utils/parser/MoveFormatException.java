package com.erikmafo.chess.utils.parser;

/**
 * Created by erikf on 16.07.2016.
 */
public class MoveFormatException extends RuntimeException {

    public MoveFormatException() {
    }

    public MoveFormatException(String message) {
        super(message);
    }

    public MoveFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public MoveFormatException(Throwable cause) {
        super(cause);
    }
}
