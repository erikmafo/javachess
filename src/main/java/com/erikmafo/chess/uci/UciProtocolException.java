package com.erikmafo.chess.uci;

/**
 * Created by erikmafo on 03.12.16.
 */
public class UciProtocolException extends Exception {

    
    public UciProtocolException() {
    }

    public UciProtocolException(String message) {
        super(message);
    }


    public UciProtocolException(String message, Throwable cause) {
        super(message, cause);
    }


    public UciProtocolException(Throwable cause) {
        super(cause);
    }


    public UciProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
