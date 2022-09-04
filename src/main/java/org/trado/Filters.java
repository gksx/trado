package org.trado;

import java.util.Optional;

import org.trado.trie.RouteTrie;

class Filters<T> {

    private final RouteTrie<T> routeTrie;

    Filters() {
        routeTrie = new RouteTrie<>();
    }

    void add(String path, int order, T filter) {
        routeTrie.insert(path, Integer.toString(order), filter);
    }

    Optional<T> get(String path, int order) {
       var requestFilter = routeTrie.action(path, Integer.toString(order));
       return Optional.ofNullable(requestFilter);
    }
}
