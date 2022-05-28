package org.trado;

import org.controller.HomeController;

public class TestUtil {
    
    static Thread currentThread;
    static Trado trado;
    public static void startInstance(){
        trado = new Trado()
            .controller("/home", HomeController.class)
            .get("/", (req) -> {
                return TradoResponse.of(String.class)
                    .content("foo")
                    .build();
            })
            .port(8080);
            
      currentThread = new Thread(trado::growl);
      currentThread.start();
    }

    public static void stop() {
        System.out.println("stopping " + currentThread.getId());
        trado.stop();
        try {
            currentThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            currentThread.interrupt();
        }
    }
}