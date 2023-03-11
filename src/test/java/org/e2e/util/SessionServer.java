package org.e2e.util;

import org.trado.Trado;
import org.trado.TradoOptions;
import org.trado.TradoResponse;

public class SessionServer {
    private Thread serverThread;
    private Trado trado;

    public void start(){

        var options = new TradoOptions()
            .withSession();

        trado = new Trado(options).port(8081)
            .get("/", (req)-> TradoResponse.empty().build());


        serverThread = new Thread(trado::growl);
        serverThread.start();
    }

    public void stop() {
        System.out.println("stopping session server " + serverThread.getName());
        trado.stop();
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("stopped thread and trado-server");
    }
}
