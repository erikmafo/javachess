package com.erikmafo.javachess.uci;

/**
 * Created by erikmafo on 04.12.16.
 */
public class InvalidMoveException extends Exception{
    public InvalidMoveException() {
    }

    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMoveException(Throwable cause) {
        super(cause);
    }

    public InvalidMoveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
