package org.trado;

public class EndRequestException extends RuntimeException {

    private final TradoResponse tradoResponse;
    public EndRequestException(TradoResponse tradoResponse) {
        this.tradoResponse = tradoResponse;
    }

    public TradoResponse getResponse() {
        return tradoResponse;
    }
}
