package org.trado;

import org.microhttp.EventLoop;
import org.trado.controller.TradoController;

public class Trado {

    private final TradoHandler tradoHandler;
    private final TradoOptions options;
    private final TradoLogger logger;
    private EventLoop eventLoop;
    
    public Trado(){
        this(new TradoOptions(), new TradoRequestLogger());
    }

    public Trado(TradoOptions options) {
        this(options, new TradoRequestLogger());
    }

    public Trado(TradoLogger tradoLogger) {
        this(new TradoOptions(), tradoLogger);
    }

    public Trado(TradoOptions options, TradoLogger tradoLogger){
        this.tradoHandler = new TradoHandler(tradoLogger);
        this.logger = tradoLogger;
        this.options = options;
    }
 
    public Trado controller(String path, Class<? extends TradoController> controllerClazz) { 
        tradoHandler.initController(path, controllerClazz);
        return this;
    }

    public Trado addStaticController(){
        tradoHandler.initController(options.staticPath(), StaticController.class);
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
        group.addRoutes(this, uri);
        return this;
    }

    public Trado requestFilter(String path, int order, RequestFilter requestFilter){
        tradoHandler.addRequestFilter(path, order, requestFilter);
        return this;
    }

    public Trado responseFilter(String path, int order, ResponseFilter responseFilter){
        tradoHandler.addRequestFilter(path, order, responseFilter);
        return this;
    }

    public void growl(){
        try {   
            this.logger.log(banner());
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
                on %s:%d""", options.host(), options.port());
    }
}
