package org.trado;

import org.microhttp.EventLoop;
import org.microhttp.Options;

public class Trado {

    private final TradoHandler tradoHandler;
    private Options options;
    private EventLoop currentEventLoop;
    
    public Trado(){
        this(new Options());
    }

    public Trado(Options options){
        this.tradoHandler = new TradoHandler();
        this.options = options;
    }
 
    public Trado controller(String path, Class<? extends TradoController> controllerClazz) { 
        tradoHandler.initController(path, controllerClazz);
        return this;
    }

    public Trado port(int port){
        options.withPort(port);
        return this;
    }

    public Trado get(String path, Action action) {
        tradoHandler.addAction(path, "GET" ,action);
        return this;
    }

    public void growl(){
        try {   
            System.out.println(banner());
            currentEventLoop = new EventLoop(options, LogFactory.logger(), tradoHandler);
            currentEventLoop.start();
        } catch (Exception e) {
            throw new TradoException("cant growl", e);
        }
    }

    public void stop(){
        currentEventLoop.stop();        
    }

    public static String banner(){
        return """
                <--------TRADO STARTING----->
                <----------GRWOLING--------->
                <-----------WAITING--------->
                """;
    }
}
