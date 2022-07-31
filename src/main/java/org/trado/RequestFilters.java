package org.trado;

import java.util.Optional;

import org.trado.trie.RouteTrie;

class RequestFilters {

    private final RouteTrie<RequestFilter> routeTrie;
    private int order = 0;

    RequestFilters() {
        routeTrie = new RouteTrie<>();
    }

    void add(String path, int order, RequestFilter requestFilter) {
        routeTrie.insert(path, Integer.toString(order), requestFilter);
    }

    Optional<RequestFilter> get(String path, int order) {
       var requestFilter = routeTrie.action(path, Integer.toString(order));
       return requestFilter != null
        ? Optional.of(requestFilter)
        : Optional.empty();

    }
    
}
