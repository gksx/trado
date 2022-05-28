package org.trado;

import org.microhttp.Logger;

public class LogFactory {
    
    private LogFactory(){
        throw new TradoException("dont even think on it");
    }

    private static Logger logger = null;

    public static Logger logger(){
        return logger == null ? 
        (logger = new TradoTraceLogger()) : 
        logger;
    }

    public static TradoLogger tradoLogger(){
        return (TradoLogger)logger();
    }
}
