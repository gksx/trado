package org.trado;

import java.lang.reflect.Method;
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
    private final Filters<RequestFilter> requestFilters;
    private final Filters<ResponseFilter> responseFilters;

    TradoHandler(TradoLogger tradoLogger) {
        this.routes = new Routes();
        this.requestFilters = new Filters<>();
        this.responseFilters = new Filters<>();
        this.executor =  Executors.newCachedThreadPool();
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
            var path = "";
            if (routeExtension != null) {
                path = uri + "/" + routeExtension.value()[0];
            } else {
                path = uri;
            }
            
            routes.add(path, httpMethodOnController, (req) -> {
                try {
                    var controllerInstance  = (TradoController)controller
                        .getDeclaredConstructors()[0]
                        .newInstance();

                    return (TradoResponse)m.invoke(controllerInstance, req);
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
        executor.execute(() -> callback.accept(internalHandle(request)));
    }

    private Response internalHandle(Request request) {
        try {
            if (tradoLogger.tradoTraceEnabled()) {
                tradoLogger.log(request.uri() + " " + request.method());
            }

            var tradoRequest = new TradoRequest(request);

            return mapResponse(tradoRequest);
        } catch (Exception e) {
            var tradoResponse = TradoController.internalError();
            tradoLogger.log(tradoResponse.httpStatus().toString());
            tradoLogger.log(e, "error internal handle");
            return tradoResponse.toResponse();
        }            
    }
    

    private Response mapResponse(TradoRequest tradoRequest) {
        try {
            requestFilters.get(tradoRequest.path(), 1)
                .ifPresent(filter -> filter.action().apply(tradoRequest));
        } catch (EndRequestException e) {
            return e.getResponse().toResponse();
        }

        var tradoResponse = routes
            .get(tradoRequest.path(), tradoRequest.request().method())
            .map(route -> {
                 if (route.containsWildCard()){
                    tradoRequest.mapRouteParams(route.wildCardPosition(), route.wildCardKey());
                 }
                 return route.action().handle(tradoRequest);
            })
            .orElse(TradoController.notFound());       

        try {
            responseFilters.get(tradoRequest.path(), 1)
                .ifPresent(filter -> filter.action().apply(tradoResponse));
        } catch (EndRequestException e) {
            return e.getResponse().toResponse();
        }    

        if (tradoLogger.tradoTraceEnabled()) {
            tradoLogger.log(tradoResponse.httpStatus().toString());
        }
        return tradoResponse.toResponse();
    }

    void addRequestFilter(String path, int order, RequestFilter requestFilter) {
        requestFilters.add(path, order, requestFilter);
    }

    public void addRequestFilter(String path, int order, ResponseFilter responseFilter) {
        responseFilters.add(path, order, responseFilter);
    }
}