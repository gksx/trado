package org.trado;

import java.util.stream.Stream;

public enum HttpStatus {
    
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"), 
    IM_A_TEAPOT(418, "I'm a teapot")
    ;

    private final int statusCode;
    private final String reason;

    private HttpStatus(int statusCode, String reason) {
        this.statusCode = statusCode;
        this.reason = reason;
    }

    public int code(){
        return statusCode;
    }

    public String reason(){
        return reason;
    }

    @Override
    public String toString(){
        return statusCode + "(" + reason + ")";
    }

    public static HttpStatus byValue(int code){
        return Stream.of(HttpStatus.values())
            .filter(e -> e.code() == code)
            .findFirst()
            .orElseThrow(()-> new TradoException("unnkown http status: " + code));
    }
}