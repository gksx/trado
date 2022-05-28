package org.trado.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.trado.TestUtil;

public class ClientTest {

    private static String baseUrl = "http://localhost:8080/";

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
        var response = request(baseUrl + "home");
        assertTrue(response.body().contains("bar"));
    }

    @Test
    public void expect_200_status() throws Exception {
        var response = request(baseUrl);
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void expect_404_status() throws Exception{
        var response = request(baseUrl + "notThere");
        assertEquals(response.statusCode(), 404);
    }

    @Test
    public void expect_foo_in_response_body() throws Exception {
        var response = request(baseUrl);
        assertTrue(response.body().contains("foo"));
    }

    private HttpResponse<String> request(String uri) throws Exception{

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
}
