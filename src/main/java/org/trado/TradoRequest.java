package org.trado;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.microhttp.Request;
/**
 * Request wrapper for some things
 */
public class TradoRequest {
    private final Request request;
    private final Map<String, String> params; 
    private final String path;

    public TradoRequest(Request request){
        this.request = request;
        params = new HashMap<>();
        this.path = request.uri().split("\\?")[0];
        mapParams();
    }

    private void mapParams() {
        queryParams();
    }    

    private void queryParams() {
        try {
            if (!this.request.uri().contains("?")){
                return;
            }
            var array = this.request.uri().split("\\?")[1].split("\\&");
            for (String keyVal : array) {
                var keyValues = keyVal.split("\\=");
                var decodedKey = URLDecoder.decode(keyValues[0], StandardCharsets.UTF_8);
                var decodedValue = URLDecoder.decode(keyValues[1], StandardCharsets.UTF_8);
                params.put(decodedKey, decodedValue);
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

    public void end() {
        throw new EndRequestException(null);
    }

    void mapRouteParams(int wildCardPosition, String wildCardKey) {
        var param = this.request.uri().split("/")[wildCardPosition];
        params.put(wildCardKey, param);
    }
}