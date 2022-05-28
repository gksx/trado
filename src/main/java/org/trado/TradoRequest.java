package org.trado;

import java.util.HashMap;
import java.util.Map;

import org.microhttp.Request;
/**
 * Request wrapper for some things
 * 
 */
public class TradoRequest {
    private final Request request;
    private Map<String, String> params; 

    public TradoRequest(Request request){
        this.request = request;
        mapParams(request.uri());
    }

    private void mapParams(String uri) {
        params = new HashMap<>();
        try {
            if (!uri.contains("?")){
                return;
            }
            var array = uri.split("\\?")[1].split("\\&");
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

    public String params(String property) {
        return params.get(property);
    }

    public Map<String, String> params(){
        return params;
    }
}
