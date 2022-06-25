package org.trado;

@FunctionalInterface
public interface RequestFilter {
    TradoRequest filter(TradoRequest tradoRequest);
}
