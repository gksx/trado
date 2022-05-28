package org.trado;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.microhttp.Handler;
import org.microhttp.Request;
import org.microhttp.Response;

class TradoHandler implements Handler {

    private final Routes routes;
    private final ExecutorService executor;
    private final static TradoLogger tradoLogger = (TradoLogger)LogFactory.logger();

    TradoHandler() {
        this.routes = new Routes();
        this.executor =  Executors.newSingleThreadExecutor();
    }

    TradoHandler initController(String uri, Class<? extends TradoController> controller){
        try {    
            var controller1  = (TradoController)controller.getDeclaredConstructors()[0].newInstance();
            for (Method m : controller.getMethods()) {
                HttpMethod httpMethod = m.getAnnotation(HttpMethod.class);
                if (httpMethod == null){
                    continue;
                }
                String httpMethodOnController = httpMethod.value()[0];
                
                routes.add(uri, httpMethodOnController, (req) -> {
                    try {
                        return (TradoResponse)m.invoke(controller1, req);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        throw new TradoException("", e);
                    }
                });
            }
       
            } catch (Exception e ) {

            }
        
        return this;
    }

    TradoHandler addAction(String uri, String method, Action action){
        routes.add(uri, method, action);
        return this;
    }

    @Override
    public void handle(Request request, Consumer<Response> callback) {
        CompletableFuture.runAsync(internalHandle(request, callback), executor);
    }

    private Runnable internalHandle(Request request, Consumer<Response> callback) {
        return () ->  { 
            tradoLogger.log(request.uri() + " " + request.method());
            
            try {
                Response response = routes
                    .get(request.uri(), request.method())
                    .map(a -> a.handle(new TradoRequest(request)).toResponse())
                    .orElse(TradoController.notFound().toResponse());
                
                callback.accept(response);    
            } catch (Exception e) {
                tradoLogger.log(e, "error internal handle");
                callback.accept(TradoController.notFound().toResponse());
            }            
        };
    }
}
