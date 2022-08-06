package org.trado;

@FunctionalInterface
public interface RequestFilter {
    void apply(TradoRequest tradoRequest);
}
