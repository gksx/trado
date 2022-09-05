package org.trado;

import java.util.Optional;

class Routes {
    private final RouteTrie<RouteAction<Action>> routeTrie;

    Routes(){
        routeTrie = new RouteTrie<RouteAction<Action>>();
    }

    void add(String path, String method, Action action) {
        RouteAction<Action> routeAction;
        if (path.contains(":")) {
            routeAction = new RouteAction<>(action, true);
        } else {
            routeAction = new RouteAction<>(action);
        }
            

        routeTrie.insert(stripParamsFromPath(path), method, routeAction);
    }

    Optional<RouteAction<Action>> get(String path, String method) {
        var action = routeTrie.action(path, method);
        return Optional.ofNullable(action);
    }

    private static String stripParamsFromPath(String path) {
        return path.split("\\?")[0];
    }
}
