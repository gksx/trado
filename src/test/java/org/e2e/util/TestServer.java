package org.e2e.util;

import org.e2e.util.controller.HomeController;
import org.trado.Trado;
import org.trado.TradoOptions;
import org.trado.TradoResponse;
import org.trado.controller.TradoController;
import org.trado.http.ContentType;

public class TestServer {
    
    static Thread serverThread;
    static Trado trado;
    public static void startInstance(){

        var options = new TradoOptions()
            .withStaticPath("/public");

        trado = new Trado(options)
            .controller("/home", HomeController.class)
            .addStaticController()
            .requestFilter("/filter", 1, (req) -> {
                TradoController.end();
            })
            .responseFilter("/after-filter", 1, (res) -> {
                res.header("x-user", "gksx");
            })
            .responseFilter("/after-filter", 2, (res) -> {
                res.header("x-token", "token");
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
            .get("redirect", (req) -> {
                return TradoResponse
                    .redirect("/filter")
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
            .get("/params/:bar", (req) -> {
                return TradoResponse.content(req.params("bar").orElse("not found")).build();
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
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("stopped thread and trado-server");
    }
}