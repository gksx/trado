package org.trado;

import org.microhttp.Options;

public class TradoOptions extends Options {
    private String staticPath = "/public";
    private String staticDirectory = "public/";
    private boolean session = false;

    public String staticDirectory() {
        return staticDirectory;
    }

    public String staticPath() {
        return staticPath;
    }

    public TradoOptions withStaticPath(String staticPath) {
        this.staticPath = staticPath;
        return this;
    }

    public TradoOptions withStaticDirectory(String staticDirectory) {
        this.staticDirectory = staticDirectory;
        return this;
    }

    public TradoOptions withSession() {
        session = true;
        return this;
    }

    public boolean useSession() {
        return session;
    }
}
