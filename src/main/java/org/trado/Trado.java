package org.trado;

import org.microhttp.EventLoop;
import org.microhttp.Logger;
import org.microhttp.Options;

public class Trado {

    private final TradoHandler tradoHandler;
    private final Options options;
    private final Logger logger;
    private EventLoop eventLoop;
    
    public Trado(){
        this(new Options(), new TradoRequestLogger());
    }

    public Trado(TradoLogger tradoLogger) {
        this(new Options(), tradoLogger);
    }

    public Trado(Options options, TradoLogger tradoLogger){
        this.tradoHandler = new TradoHandler(tradoLogger);
        this.logger = tradoLogger;
        this.options = options;
    }
 
    public Trado controller(String path, Class<? extends TradoController> controllerClazz) { 
        tradoHandler.initController(path, controllerClazz);
        return this;
    }

    public Trado usePublicController(){
        tradoHandler.initController("/public", PublicController.class);
        return this;
    }

    public Trado port(int port){
        options.withPort(port);
        return this;
    }

    public Trado get(String uri, Action action) {
        tradoHandler.addAction(uri, "GET" ,action);
        return this;
    }

    public Trado post(String uri, Action action) {
        tradoHandler.addAction(uri, "POST", action);
        return this;
    }

    public Trado put(String uri, Action action) {
        tradoHandler.addAction(uri, "PUT", action);
        return this;
    }

    public Trado delete(String uri, Action action) {
        tradoHandler.addAction(uri, "DELETE", action);
        return this;
    }

    public Trado path(String uri, Group group){
        group.addRoutes(this);
        return this;
    }

    public Trado requestFilter(String path, int order, RequestFilter requestFilter){
        tradoHandler.addRequestFilter(path, order, requestFilter);
        return this;
    }

    public Trado responseFilter(ResponseFilter responseFilter){
        return this;
    }

    public void growl(){
        try {   
            System.out.println(banner());
            eventLoop = new EventLoop(options, logger, tradoHandler);
            eventLoop.start();
        } catch (Exception e) {
            throw new TradoException("cant growl", e);
        }
    }

    public void stop(){
        eventLoop.stop();        
    }

    private String banner(){
        return String.format("""
                <--------------------------->
                <--------TRADO STARTING----->
                <----------GRWOLING!-------->
                <--------------------------->
                           >on %d<""", options.port());
    }
}
