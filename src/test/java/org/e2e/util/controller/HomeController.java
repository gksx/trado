package org.e2e.util.controller;

import org.trado.TradoException;
import org.trado.TradoRequest;
import org.trado.TradoResponse;
import org.trado.controller.HttpMethod;
import org.trado.controller.Route;
import org.trado.controller.TradoController;
import org.trado.http.Method;

import static org.trado.TradoResponse.*;

public class HomeController extends TradoController {

    @HttpMethod(Method.GET)
    public TradoResponse index(TradoRequest request){
        
        return content(request.params("q").orElse("foo"))
            .build();
    }

    @HttpMethod(Method.POST)
    public TradoResponse echo(TradoRequest request){
        return content(new String(request.request().body()))
            .build();
    }

    @Route("away")
    @HttpMethod(Method.GET)
    public TradoResponse away(TradoRequest request) {
        return empty().build();
    }

    @Route("error")
    @HttpMethod(Method.GET)
    public TradoResponse error(TradoRequest request) {
        throw new TradoException("test");
    }

    @Route("halt")
    @HttpMethod(Method.GET)
    public TradoResponse halt(TradoRequest request){
        return end();
    }
}
