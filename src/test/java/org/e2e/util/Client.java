package org.e2e.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

import org.trado.TradoException;

public class Client {
    
    private static HttpClient httpClient = HttpClient.newHttpClient();

    public static HttpResponse<String> getRequest(String uri) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .GET()
            .build();

        try {
            return httpClient.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new TradoException(ex);
        }
            
    }

    public static HttpResponse<String> postRequest(String body, String uri) {
         HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .POST(BodyPublishers.ofString(body))
            .build();
        
        try {
            return httpClient
                .send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new TradoException(ex);
        }
        
    }

    public static HttpResponse<String> getRequest(String uri, String cookie) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .setHeader("cookie", cookie)
            .GET()
            .build();

        try {
            return httpClient.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new TradoException(ex);
        }
            
    }

    public static CompletableFuture<HttpResponse<String>> getAsync(String uri) {
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .GET()
            .build();

        return httpClient.sendAsync(request, BodyHandlers.ofString());
    }
    
}
