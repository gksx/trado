package org.trado;

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
    private final TradoLogger tradoLogger;

    TradoHandler(TradoLogger tradoLogger) {
        this.routes = new Routes();
        this.executor =  Executors.newSingleThreadExecutor();
        this.tradoLogger = tradoLogger;
    }

    TradoHandler initController(String uri, Class<? extends TradoController> controller){    
        for (Method m : controller.getMethods()) {
            var httpMethod = m.getAnnotation(HttpMethod.class);
            if (httpMethod == null){
                continue;
            }
            var httpMethodOnController = httpMethod.value()[0];

            var routeExtension = m.getAnnotation(Route.class);
            String path = "";
            if (routeExtension != null) {
                path = uri + "/" + routeExtension.value()[0];
            } else {
                path = uri;
            }
            
            routes.add(path, httpMethodOnController, (req) -> {
                try {
                    var controller1  = (TradoController)controller
                        .getDeclaredConstructors()[0]
                        .newInstance();

                    return (TradoResponse)m.invoke(controller1, req);
                } catch (Exception e) {
                    throw new TradoException("Invoking action", e.getCause());
                }
            });
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
            try {
                if (tradoLogger.tradoTraceEnabled()) {
                    tradoLogger.log(request.uri() + " " + request.method());
                }

                var tradoRequest = new TradoRequest(request);
                var tradoResponse = routes
                    .get(tradoRequest.path(), tradoRequest.request().method())
                    .map(a -> a.handle(tradoRequest))
                    .orElse(TradoController.notFound());

                if (tradoLogger.tradoTraceEnabled()) {
                    tradoLogger.log(tradoResponse.httpStatus().toString());
                }
                
                callback.accept(tradoResponse.toResponse());    
            } catch (Exception e) {
                var tradoResponse = TradoController.internalError();
                tradoLogger.log(tradoResponse.httpStatus().toString());
                tradoLogger.log(e, "error internal handle");
                callback.accept(tradoResponse.toResponse());
            }            
        };
    }
}
