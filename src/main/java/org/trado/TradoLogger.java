package org.trado;


public interface TradoLogger {
    public void log(String entry);
    public void log(Exception e, String entry);
}
