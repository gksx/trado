package org.trado;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.microhttp.Request;

public class TradoRequestTest {
    
    @Test
    public void params_from_url(){
        var request = new Request(
            "GET", 
            "http://localhost:8080?foo=bar&bar=foo", 
            "1.1",
            new ArrayList<>(), 
            null);
        
        var tradoRequest = new TradoRequest(request);
        assertTrue(tradoRequest.params().size() == 2);
        assertTrue(tradoRequest.params("foo").equals("bar"));
        assertTrue(tradoRequest.params("bar").equals("foo"));
    }

    @Test
    public void only_one_params(){
        var request = new Request(
            "GET", 
            "http://localhost:8080?bar=foo", 
            "1.1",
            new ArrayList<>(), 
            null);
        
        var tradoRequest = new TradoRequest(request);
        assertTrue(tradoRequest.params().size() == 1);
        assertTrue(tradoRequest.params("bar").equals("foo"));   
    }

    @Test
    public void no_params(){
        var request = new Request(
            "GET", 
            "http://localhost:8080/error", 
            "1.1",
            new ArrayList<>(), 
            null);
        
        var tradoRequest = new TradoRequest(request);
        assertTrue(tradoRequest.params().size() == 0);
    }
}
