package org.util;

import org.trado.ContentType;
import org.trado.Trado;
import org.trado.TradoController;
import org.trado.TradoResponse;
import org.util.controller.HomeController;

public class TestServer {
    
    static Thread serverThread;
    static Trado trado;
    public static void startInstance(){
        trado = new Trado()
            .controller("/home", HomeController.class)
            .usePublicController()
            .requestFilter("/filter", 1, (req) -> {
                TradoController.end();
            })
            .responseFilter("/after-filter", 1, (res) -> {
                res.header("x-user", "gksx");
            })
            .get("/filter", (req) -> {
                return TradoResponse.empty().build();
            })
            .get("/after-filter", (req) -> {
                return TradoResponse.empty().build();
            })
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
            .path("/group", (trado, path) -> {                
                trado.get(path, (req) -> {
                    return TradoResponse.empty().build();
                });
                trado.post(path, (req) -> TradoResponse.empty().build());
            })
            .path("/threads", (trado, path) -> {
                trado.get(path, (req) -> {
                    try {
                        System.out.println(Thread.currentThread().getName() + "is sleeping");
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                    }
                    return TradoResponse.empty().build();
                });
                trado.post(path, (req) -> {
                    try {
                        System.out.println(Thread.currentThread().getName() + "is sleeping");
                    } catch (Exception e) {
                    }
                    return TradoResponse.empty().build();
                });
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
}