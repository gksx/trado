package org.trado;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.microhttp.Header;
import org.microhttp.Request;

public class TradoRequestTest {

    static byte[] embtyByteArray = {};
    
    @Test
    public void params_from_url() {
        var request = new Request(
            "GET", 
            "http://localhost:8080?foo=bar&bar=foo", 
            "1.1",
            new ArrayList<>(), 
            embtyByteArray);
        
        var tradoRequest = new TradoRequest(request, new TradoOptions());
        assertTrue(tradoRequest.params().size() == 2);
        assertTrue(tradoRequest.params("foo").get().equals("bar"));
        assertTrue(tradoRequest.params("bar").get().equals("foo"));
    }

    @Test
    public void only_one_params() {
        var request = new Request(
            "GET", 
            "http://localhost:8080?bar=foo", 
            "1.1",
            new ArrayList<>(), 
            embtyByteArray);
        
        var tradoRequest = new TradoRequest(request, new TradoOptions());
        assertTrue(tradoRequest.params().size() == 1);
        assertTrue(tradoRequest.params("bar").get().equals("foo"));   
    }

    @Test
    public void no_params() {
        var request = new Request(
            "GET", 
            "http://localhost:8080/error", 
            "1.1",
            new ArrayList<>(), 
            embtyByteArray);
        
        var tradoRequest = new TradoRequest(request, new TradoOptions());
        assertTrue(tradoRequest.params().size() == 0);
    }

    @Test
    public void coookies() {
        List<Header> headers = List.of(new Header("cookie", "trado-session-id=123123012"));
        var request = new Request(
            "GET", 
            "http://localhost:8080", 
            "1.1",
            headers, 
            embtyByteArray);
        
        var tradoRequest = new TradoRequest(request, new TradoOptions());
        assertTrue(tradoRequest.cookie("trado-session-id").isPresent());
    }
}
