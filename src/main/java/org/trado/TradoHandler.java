package org.trado;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.microhttp.Handler;
import org.microhttp.Request;
import org.microhttp.Response;
import org.trado.controller.HttpMethod;
import org.trado.controller.Route;
import org.trado.controller.TradoController;
import org.trado.http.Method;
import org.trado.session.SessionRequestFilter;
import org.trado.session.SessionResponseFilter;

class TradoHandler implements Handler {

    private final Routes routes;
    private final ExecutorService executor;
    private final TradoLogger tradoLogger;
    private final Filters<RequestFilter> requestFilters;
    private final Filters<ResponseFilter> responseFilters;
    private final TradoOptions tradoOptions;

    TradoHandler(TradoLogger tradoLogger, TradoOptions tradoOptions) {
        this.tradoOptions = tradoOptions;
        this.routes = new Routes();
        this.requestFilters = new Filters<>();
        this.responseFilters = new Filters<>();
        this.executor = Executors.newCachedThreadPool();
        this.tradoLogger = tradoLogger;
        if (tradoOptions.useSession()) {
            this.requestFilters.add("/", new SessionRequestFilter()::apply);
            this.responseFilters.add("/", new SessionResponseFilter()::apply);
        }
    }

    void initController(String uri, Class<? extends TradoController> controller) {
        for (java.lang.reflect.Method m : controller.getMethods()) {
            var httpMethod = m.getAnnotation(HttpMethod.class);
            if (httpMethod == null) {
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

            routes.add(path, httpMethodOnController.name(), (req) -> {
                try {
                    var controllerInstance = (TradoController) controller
                            .getDeclaredConstructors()[0]
                            .newInstance();

                    return (TradoResponse) m.invoke(controllerInstance, req);
                } catch (Exception e) {
                    throw new TradoException("Invoking action", e);
                }
            });
        }
    }

    void addAction(String uri, Method method, Action action) {
        routes.add(uri, method.name(), action);
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

            var tradoRequest = new TradoRequest(request, tradoOptions);

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
            var numberOfRequestFilters = requestFilters.size(tradoRequest.path());
            for (int i = 1; i < numberOfRequestFilters + 1; i++) {
                requestFilters.get(tradoRequest.path(), i)
                        .ifPresent(filter -> filter.action().apply(tradoRequest));
            }
        } catch (EndRequestException e) {
            return e.getResponse().toResponse();
        }

        var tradoResponse = routes
                .get(tradoRequest.path(), tradoRequest.request().method())
                .map(route -> {
                    if (route.containsWildCard()) {
                        tradoRequest.mapRouteParams(route.wildCardPosition(), route.wildCardKey());
                    }
                    return route.action().handle(tradoRequest);
                })
                .orElse(TradoController.notFound());

        try {
            var numberOfResponseFilters = responseFilters.size(tradoRequest.path());
            for (int i = 1; i < numberOfResponseFilters + 1; i++) {
                responseFilters.get(tradoRequest.path(), i)
                        .ifPresent(filter -> filter.action().apply(tradoResponse));
            }
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

    void addResponseFilter(String path, int order, ResponseFilter responseFilter) {
        responseFilters.add(path, order, responseFilter);
    }
}