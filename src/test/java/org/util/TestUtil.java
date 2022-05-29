package org.util;

import org.controller.HomeController;
import org.trado.Trado;
import org.trado.TradoResponse;

public class TestUtil {
    
    static Thread serverThread;
    static Trado trado;
    public static void startInstance(){
        trado = new Trado()
            .controller("/home", HomeController.class)
            .get("/", (req) -> {
                return TradoResponse.content("foo")
                    .build();
            })
            .post("/echo", (req) -> {
                return TradoResponse
                    .content(new String(req.request().body()))
                    .build();
            })
            .get("/empty", (req) -> {
                return TradoResponse
                    .empty()
                    .build();
            })
            .port(8080);
            
      serverThread = new Thread(trado::growl);
      serverThread.start();
    }

    public static void stop() {
        System.out.println("stopping " + serverThread.getId());
        trado.stop();
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("stopped thread and trado-server");
    }
}