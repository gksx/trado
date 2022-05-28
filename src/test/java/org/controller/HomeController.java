package org.controller;

import org.microhttp.Request;
import org.trado.HttpMethod;
import org.trado.TradoController;
import org.trado.TradoResponse;

public class HomeController extends TradoController {
    @HttpMethod("GET")
    public TradoResponse index(Request request){
        return TradoResponse.of(String.class)
            .content("bar")
            .build();
    }
}
