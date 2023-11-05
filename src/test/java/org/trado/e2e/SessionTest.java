package org.trado.e2e;

import java.util.UUID;

import org.trado.e2e.util.Client;
import org.trado.e2e.util.SessionServer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SessionTest {
    
    static SessionServer server = new SessionServer();


    public static void startServer() {
        server.start();
    }

    public static void stopServer() {
        server.stop();
    }

    @Test
    @Disabled
    public void test() throws InterruptedException {
        var q = Client.getRequest("http://localhost:8081/", "trado-session=" + UUID.randomUUID());
        System.out.println(q.statusCode());
    }
}
