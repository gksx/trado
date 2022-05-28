package org.trado;

import org.microhttp.Request;
/**
 * Request wrapper for some things
 * 
 */
public class TradoRequest {
    private final Request request;

    public TradoRequest(Request request){
        this.request = request;
    }

    public Request request(){
        return request;
    }
}
