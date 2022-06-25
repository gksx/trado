package org.util;

import org.controller.HomeController;
import org.trado.Action;
import org.trado.ContentType;
import org.trado.Trado;
import org.trado.TradoResponse;

public class TestServer {
    
    static Thread serverThread;
    static Trado trado;
    public static void startInstance(){
        trado = new Trado()
            .controller("/home", HomeController.class)
            .usePublicController()
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
            .get("/json", (req) -> {
                return TradoResponse
                .empty()
                .contentType(ContentType.APPLICATION_JSON)
                .build();
            })
            .port(8080);
            
      serverThread = new Thread(trado::growl);
      serverThread.start();
    }

    public static void stop() {
        System.out.println("stopping " + serverThread.getName());
        trado.stop();
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("stopped thread and trado-server");
    }

    public static void test(Action action) {
        new Trado()
        .path("/hej", (trado)-> {
            
            trado.get("", (req) -> {
                return TradoResponse
                .empty()
                .contentType(ContentType.APPLICATION_JSON)
                .build();
            });

            trado.post("uri", (req) -> {
                return TradoResponse
                .empty()
                .contentType(ContentType.APPLICATION_JSON)
                .build();
            });
        })
        .growl();
        
    }
}