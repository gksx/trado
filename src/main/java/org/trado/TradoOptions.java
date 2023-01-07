package org.trado;

import org.microhttp.Options;

public class TradoOptions extends Options {
    private String staticPath = "/public";
    public String staticPath() {
        return staticPath;
    }

    public TradoOptions withStaticPath(String path) {
        this.staticPath = path;
        return this;
    }
}
