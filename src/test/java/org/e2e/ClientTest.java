package org.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.util.TestUtil;

public class ClientTest {

    private static String baseUrl = "http://localhost:8080";

    @BeforeAll
    public static void initServer(){
        TestUtil.startInstance();
    }

    @AfterAll
    public static void stop(){
        TestUtil.stop();
    }

    @Test
    public void expect_bar_from_controller() throws Exception{
        var response = empyGetRequest(baseUrl + "/home");
        assertTrue(response.body().contains("bar"));
    }

    @Test
    public void expect_200_status() throws Exception {
        var response = empyGetRequest(baseUrl);
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void expect_404_status() throws Exception{
        var response = empyGetRequest(baseUrl + "/notThere");
        assertEquals(response.statusCode(), 404);
    }

    @Test
    public void expect_foo_in_response_body() throws Exception {
        var response = empyGetRequest(baseUrl);
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

    private HttpResponse<String> empyGetRequest(String uri) throws Exception{

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(uri))
                .GET()
                .build();

        return HttpClient.newHttpClient()
            .send(request, BodyHandlers.ofString());
        } catch (Exception e) { 
            throw e;
        }
    }

    private HttpResponse<String> postRequest(String body, String uri) throws Exception{
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(uri))
                .POST(BodyPublishers.ofString(body))
                .build();

        return HttpClient.newHttpClient()
            .send(request, BodyHandlers.ofString());
        } catch (Exception e) { 
            throw e;
        }
    }
}