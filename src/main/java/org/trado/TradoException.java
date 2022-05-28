package org.trado;

public class TradoException extends RuntimeException {

    public TradoException(String message) {
        super(message);
    }

    public TradoException(String message, Throwable cause) {
        super(message, cause);
    }
}

