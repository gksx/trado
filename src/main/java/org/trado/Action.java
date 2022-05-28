package org.trado;

@FunctionalInterface
public interface Action {
    TradoResponse handle(TradoRequest request);
}
