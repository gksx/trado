package org.trado;

import org.microhttp.Request;

@FunctionalInterface
public interface Action {
    TradoResponse handle(Request request);
}
