package org.trado.http;

import java.util.stream.Stream;

import org.trado.TradoException;

public enum Status {
    
    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"), 
    IM_A_TEAPOT(418, "I'm a teapot"),
    INTERNAL_ERROR(500, "Internal Error"), 
    TEMPRORAY_REDIRECT(307, "Temporary Redirect"),
    ;

    private final int statusCode;
    private final String reason;

    Status(int statusCode, String reason) {
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

    public static Status byValue(int code) {
        return Stream.of(Status.values())
            .filter(e -> e.code() == code)
            .findFirst()
            .orElseThrow(()-> new TradoException("unnkown http status: " + code));
    }
}