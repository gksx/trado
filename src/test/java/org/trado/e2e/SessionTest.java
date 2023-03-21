package org.trado.e2e;

import java.util.UUID;

import org.trado.e2e.util.Client;
import org.trado.e2e.util.SessionServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SessionTest {
    
    static SessionServer server = new SessionServer();


    @BeforeAll
    public static void startServer() {
        server.start();
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
    }


    @Test
    public void test() throws InterruptedException {
        var q = Client.getRequest("http://localhost:8081/", "trado-session=" + UUID.randomUUID());
        System.out.println(q.statusCode());
    }
}
