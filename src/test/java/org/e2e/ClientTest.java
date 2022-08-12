package org.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.trado.ContentType;
import org.util.TestServer;

public class ClientTest {

    private static String baseUrl = "http://localhost:8080";
    HttpClient httpClient = HttpClient.newHttpClient();

    @BeforeAll
    public static void initServer(){
        TestServer.startInstance();
    }

    @AfterAll
    public static void stop(){
        TestServer.stop();
    }

    @Test
    public void expect_bar_from_controller() throws Exception{
        var response = getRequest(baseUrl + "/home");
        assertTrue(response.body().contains("bar"));
    }

    @Test
    public void expect_200_status() throws Exception {
        var response = getRequest(baseUrl);
        assertEquals(response.statusCode(), 200);
        assertTrue(response.headers().firstValue("Content-Type").isPresent());
    }

    @Test
    public void expect_404_status() throws Exception{
        var response = getRequest(baseUrl + "/notThere");
        assertEquals(response.statusCode(), 404);
        assertTrue(response.body().contains("404"));
    }

    @Test
    public void expect_200_wiht_queryParams() throws Exception {
        var response = getRequest(baseUrl + "/home?q=foo");
        assertTrue(response.body().contains("bar"));
    }

    @Test
    public void expect_foo_in_response_body() throws Exception {
        var response = getRequest(baseUrl);
        assertTrue(response.body().contains("foo"));
    }

    @Test
    public void expect_echo_in_response_body_from_controller() throws Exception {
        var response = postRequest("hello!", baseUrl + "/home");
        assertTrue(response.body().equalsIgnoreCase("hello!"));
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void excpect_echo_in_response_body_from_action() throws Exception {
        var response = postRequest("hello!", baseUrl + "/echo");
        assertTrue(response.body().equalsIgnoreCase("hello!"));
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void expect_empty_response() throws Exception {
        var response = getRequest(baseUrl + "/empty");
        assertTrue(response.body().length() == 0);
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void expect_internal_error() throws Exception {
        var response = getRequest(baseUrl + "/home/error");
        assertEquals(500, response.statusCode());
        assertTrue(response.body().contains("500"));
    }

    @Test
    public void expect_content_type_application_json() throws Exception {
        var response = getRequest(baseUrl + "/json");
        assertTrue(response.headers().firstValue("Content-Type").get().equals(ContentType.APPLICATION_JSON));
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void expect_app_js_in_return() throws Exception{
        var response = getRequest(baseUrl + "/public/app.js");
        assertTrue(response.body().contains("hello world"));
        assertTrue(response.headers().firstValue("Content-Type").get().equals(ContentType.APPLICATION_JAVASCRIPT));
    }

    @Test
    public void expect_away_route() throws Exception{
        var response = getRequest(baseUrl + "/home/away");
        assertEquals(200, response.statusCode());
    }

    @Test
    public void expect_end_from_filter() throws Exception{
        var response = getRequest(baseUrl + "/filter");
        assertEquals(404, response.statusCode());
    }

    @Test
    public void expect_header_from_filter() throws Exception {
        var response = getRequest(baseUrl + "/after-filter");
        var header = response.headers().firstValue("x-user").get();
        assertEquals("gksx", header);
    }

    @Test
    public void expect_header_from_filter_not_existing() throws Exception {
        var response = getRequest(baseUrl + "/home");
        var header = response.headers().firstValue("x-user");
        assertFalse(header.isPresent());
    }

    @Test
    public void some_100_or_so_requests_in_parallel() throws Exception{
         Callable<?> callable = () -> {
            return new ForkJoinPool(100).submit(()-> {
                IntStream.range(0, 50).parallel().forEach(i -> {
                    try {
                        var s = getRequest(baseUrl + "/home").body();
                        assertNotNull(s);
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }).get();
         };

         logAndRun(callable);
    }

    private void logAndRun(Callable<?> callable) throws Exception {
        long startTime = System.currentTimeMillis();
        callable.call();
        System.out.println(("took " + (System.currentTimeMillis() - startTime) + " milliseconds"));
    }

    @Test
    public void some_async_requests_after_another() throws Exception{
        var numberOfRequests = 100;
        var array = new CompletableFuture[numberOfRequests];
        for (int i = 0; i < numberOfRequests; i++) {
            array[i] = getAsync(baseUrl + "/home");    
        }

        CompletableFuture.allOf(array).join();
    }

    private CompletableFuture<HttpResponse<String>> getAsync(String uri) throws Exception{
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(uri))
            .GET()
            .build();

        return httpClient.sendAsync(request, BodyHandlers.ofString());
        
    }

    private HttpResponse<String> getRequest(String uri) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .GET()
            .build();

        return httpClient.send(request, BodyHandlers.ofString());        
    }

    private HttpResponse<String> postRequest(String body, String uri) throws Exception{
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .POST(BodyPublishers.ofString(body))
            .build();
        
        return HttpClient.newHttpClient()
            .send(request, BodyHandlers.ofString());
        
    }
}