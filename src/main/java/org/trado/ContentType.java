package org.trado;

public class ContentType {
    
    private ContentType(){
        throw new TradoException("dont even try to instatitae");
    }

    public static final String APPLICATION_JSON = "application/json";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_HTML = "text/html";
}
