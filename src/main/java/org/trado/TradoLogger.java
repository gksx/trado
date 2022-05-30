package org.trado;

import org.microhttp.Logger;

public abstract class TradoLogger implements Logger{
    abstract void log(String entry);
    abstract void log(Exception e, String entry);
}
