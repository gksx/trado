package org.trado;

public class EndRequestException extends RuntimeException {

    private final TradoRequest tradoRequest;
    public EndRequestException(TradoRequest tradoRequest) {
        this.tradoRequest = tradoRequest;
    }

    public TradoRequest getRequest() {
        return tradoRequest;
    }
    
}
