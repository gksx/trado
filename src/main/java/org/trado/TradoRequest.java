package org.trado;

import java.util.HashMap;
import java.util.Map;

import org.microhttp.Header;
import org.microhttp.Request;
/**
 * Request wrapper for some things
 * 
 */
public class TradoRequest {
    private final Request request;
    private final Map<String, String> params; 
    private final String path;

    public TradoRequest(Request request){
        this.request = request;
        params = new HashMap<>();
        mapParams();
        this.path = request.uri().split("\\?")[0];
    }

    private void mapParams() {
        try {
            if (!this.request.uri().contains("?")){
                return;
            }
            var array = this.request.uri().split("\\?")[1].split("\\&");
            for (String keyVal : array) {
                var keyValues = keyVal.split("\\=");
                params.put(keyValues[0], keyValues[1]);      
            }
        }
        catch (Exception ex) {
            //supress we dont care about malformed url params
        }
    }

    public Request request(){
        return request;
    }

    public String path(){
        return path;
    }

    public String params(String property) {
        return params.get(property);
    }

    public Map<String, String> params(){
        return params;
    }

    public void addHeader(String name, String value) {
        request().headers().add(new Header(name, value));
    }

    public void end() {
        throw new EndRequestException();
    }
}
