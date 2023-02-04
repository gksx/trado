package org.trado;

import org.microhttp.Options;

public class TradoOptions extends Options {
    private String staticPath = "/public";
    private String staticDirectory = "public/";

    public String staticDirectory() {
        return staticDirectory;
    }

    public String staticPath() {
        return staticPath;
    }

    public TradoOptions withStaticPath(String path) {
        this.staticPath = path;
        return this;
    }

    public TradoOptions withStaticDirectory(String path) {
        this.staticDirectory = path;
        return this;
    }
}
