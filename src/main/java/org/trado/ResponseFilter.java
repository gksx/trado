package org.trado;

@FunctionalInterface
public interface ResponseFilter {
    void apply(TradoResponse tradoResponse);
}
