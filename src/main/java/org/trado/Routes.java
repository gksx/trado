package org.trado;

import java.util.Optional;

import org.trado.trie.RouteTrie;

class Routes {
    private final RouteTrie routeTrie;

    Routes(){
        routeTrie = new RouteTrie();
    }

    void add(String path, String method, Action action){        
        routeTrie.insert(stripParamsFromPath(path), method, action);
    }

    Optional<Action> get(String path, String method) {
        var action = routeTrie.action(path, method);
        return action != null 
            ? Optional.of(action)
            : Optional.empty();
    }

    private static String stripParamsFromPath(String path) {
        return path.split("\\?")[0];
    }
}
