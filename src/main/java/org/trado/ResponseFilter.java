package org.trado;

@FunctionalInterface
public interface ResponseFilter {
    TradoResponse filter(TradoResponse tradoResponse);
}
