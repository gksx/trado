package org.trado;

import java.util.Optional;

class Filters<T> {

    private final RouteTrie<RouteAction<T>> routeTrie;

    Filters() {
        routeTrie = new RouteTrie<>();
    }

    void add(String path, int order, T filter) {
        routeTrie.insert(path, Integer.toString(order), new RouteAction<T>(filter));
    }

    Optional<RouteAction<T>> get(String path, int order) {
       var requestFilter = routeTrie.action(path, Integer.toString(order));
       return Optional.ofNullable(requestFilter);
    }

    int size(String path) {
        return routeTrie.size(path);
    }
}
