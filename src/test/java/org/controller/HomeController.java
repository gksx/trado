package org.controller;

import org.trado.HttpMethod;
import org.trado.Route;
import org.trado.TradoController;
import org.trado.TradoException;
import org.trado.TradoRequest;
import org.trado.TradoResponse;

public class HomeController extends TradoController {

    @HttpMethod("GET")
    public TradoResponse index(TradoRequest request){
        return TradoResponse.content("bar")
            .build();
    }

    @HttpMethod("POST")
    public TradoResponse echo(TradoRequest request){
        return TradoResponse.content(new String(request.request().body()))
            .build();
    }

    @Route("away")
    @HttpMethod("GET")
    public TradoResponse away(TradoRequest request) {
        return TradoResponse.empty().build();
    }

    @Route("error")
    @HttpMethod("GET")
    public TradoResponse error(TradoRequest request) {
        // return TradoResponse.empty().build();
        throw new TradoException("test");
    }
}
