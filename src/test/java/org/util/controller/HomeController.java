package org.util.controller;

import org.trado.HttpMethod;
import org.trado.Route;
import org.trado.TradoController;
import org.trado.TradoException;
import org.trado.TradoRequest;
import org.trado.TradoResponse;

import static org.trado.TradoResponse.*;

public class HomeController extends TradoController {

    @HttpMethod("GET")
    public TradoResponse index(TradoRequest request){
        return content("bar")
            .build();
    }

    @HttpMethod("POST")
    public TradoResponse echo(TradoRequest request){
        return content(new String(request.request().body()))
            .build();
    }

    @Route("away")
    @HttpMethod("GET")
    public TradoResponse away(TradoRequest request) {
        return empty().build();
    }

    @Route("error")
    @HttpMethod("GET")
    public TradoResponse error(TradoRequest request) {
        throw new TradoException("test");
    }
}
