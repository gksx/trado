package org.e2e;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.e2e.util.Client.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

import org.e2e.util.TestServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.trado.http.ContentType;

public class ClientTest {

    private static String baseUrl = "http://localhost:8080";
    

    @BeforeAll
    public static void initServer(){
        TestServer.startInstance();
    }

    @AfterAll
    public static void stop(){
        TestServer.stop();
    }

    @Test
    public void expect_200_status() {
        var response = getRequest(baseUrl);
        assertEquals(response.statusCode(), 200);
        assertTrue(response.headers().firstValue("Content-Type").isPresent());
    }

    @Test
    public void expect_404_status() {
        var response = getRequest(baseUrl + "/notThere");
        assertEquals(response.statusCode(), 404);
        assertTrue(response.body().contains("404"));
    }

    @Test
    public void expect_200_wiht_queryParams() {
        var response = getRequest(baseUrl + "/home?q=foo");
        assertTrue(response.body().contains("foo"));
    }

    @Test
    public void expect_decoded_query_param() {
        var encodedParam = URLEncoder.encode("foo and bar", StandardCharsets.UTF_8);
        var response = getRequest(baseUrl + "/home?q=" + encodedParam);
        assertTrue(response.body().contains("foo and bar"));
    }

    @Test
    public void expect_foo_in_response_body() {
        var response = getRequest(baseUrl);
        assertTrue(response.body().contains("foo"));
    }

    @Test
    public void expect_echo_in_response_body_from_controller()  {
        var response = postRequest("hello!", baseUrl + "/home");
        assertTrue(response.body().equalsIgnoreCase("hello!"));
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void excpect_echo_in_response_body_from_action()  {
        var response = postRequest("hello!", baseUrl + "/echo");
        assertTrue(response.body().equalsIgnoreCase("hello!"));
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void expect_empty_response()  {
        var response = getRequest(baseUrl + "/empty");
        assertTrue(response.body().length() == 0);
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void expect_internal_error()  {
        var response = getRequest(baseUrl + "/home/error");
        assertEquals(500, response.statusCode());
        assertTrue(response.body().contains("500"));
    }

    @Test
    public void expect_content_type_application_json()  {
        var response = getRequest(baseUrl + "/json");
        assertTrue(response.headers().firstValue("Content-Type").get().equals(ContentType.APPLICATION_JSON));
        assertEquals(response.statusCode(), 200);
    }

    @Test
    public void expect_app_js_in_return() {
        var response = getRequest(baseUrl + "/public/app.js");
        assertTrue(response.body().contains("hello world"));
        assertTrue(response.headers().firstValue("Content-Type").get().equals(ContentType.APPLICATION_JAVASCRIPT));
    }

    @Test
    public void expect_style_css_in_return() {
        var response = getRequest(baseUrl + "/public/styles.css");
        assertTrue(response.body().contains("body"));
        assertTrue(response.headers().firstValue("Content-Type").get().equals(ContentType.TEXT_CSS));
    }

    @Test
    public void expect_away_route() {
        var response = getRequest(baseUrl + "/home/away");
        assertEquals(200, response.statusCode());
    }

    @Test
    public void expect_end_from_filter() {
        var response = getRequest(baseUrl + "/filter");
        assertEquals(404, response.statusCode());
    }

    @Test
    public void expect_header_from_filter()  {
        var response = getRequest(baseUrl + "/after-filter");
        var header = response.headers().firstValue("x-user").get();
        assertEquals("gksx", header);
    }

    @Test
    public void expect_second_header_from_filter()  {
        var response = getRequest(baseUrl + "/after-filter");
        var header = response.headers().firstValue("x-token").get();
        assertEquals("token", header);
    }

    @Test
    public void expect_header_from_filter_not_existing()  {
        var response = getRequest(baseUrl + "/home");
        var header = response.headers().firstValue("x-user");
        assertFalse(header.isPresent());
    }

    @Test
    public void expect_emmpty_from_group()  {
        var response = getRequest(baseUrl + "/group");
        assertTrue(response.body().length() == 0);
        assertTrue(response.statusCode() == 200);
    }

    @Test
    public void expect_emmpty_from_group_post()  {
        var response = postRequest("",baseUrl + "/group");
        assertTrue(response.body().length() == 0);
        assertTrue(response.statusCode() == 200);
    }

    @Test
    public void expect_delay_from_thread_sleep()  {
        var numberOfRequests = 2;
        var executor = Executors.newFixedThreadPool(2);
        var array = new CompletableFuture[numberOfRequests];
        
        array[0] = CompletableFuture.runAsync(()-> {
            try {
                getRequest(baseUrl + "/threads");
            } catch (Exception e) {
                e.printStackTrace();
            }
            }, executor);
        array[1] = CompletableFuture.runAsync(()-> {
            try {
                postRequest("", baseUrl + "/threads");
            } catch (Exception e) {
                e.printStackTrace();
            }
            }, executor);
        
        CompletableFuture.allOf(array).join();
    }

    @Test
    public void some_50_or_so_requests_in_parallel() throws Exception {
         Callable<?> callable = () -> {
            return new ForkJoinPool(50).submit(()-> {
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
    public void some_async_requests_after_another() {
        var numberOfRequests = 50;
        var array = new CompletableFuture[numberOfRequests];
        for (int i = 0; i < numberOfRequests; i++) {
            array[i] = getAsync(baseUrl + "/home");    
        }

        CompletableFuture.allOf(array).join();
    }

    @Test
    public void test_route_params()  {
        var request = getRequest(baseUrl + "/params/foo");
        assertTrue(request.body().contains("foo"));
    }

}