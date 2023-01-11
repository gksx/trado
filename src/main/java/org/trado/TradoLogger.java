package org.trado;

import org.microhttp.Logger;

public interface TradoLogger extends Logger {
    void log(String entry);
    void log(Exception e, String entry);
    boolean tradoTraceEnabled();
}
