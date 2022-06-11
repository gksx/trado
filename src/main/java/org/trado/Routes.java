package org.trado;

import java.util.Optional;

import org.trado.trie.RouteTrie;

class Routes {
    private final RouteTrie routeTrie;

    Routes(){
        routeTrie = new RouteTrie();
    }

    void add(String path, String method, Action action){        
        routeTrie.insert(pathWitNoParams(path), method, action);
    }

    Optional<Action> get(String path, String method) {
        var action = routeTrie.action(pathWitNoParams(path), method);
        return action != null 
            ? Optional.of(action)
            : Optional.empty();
    }

    private static String pathWitNoParams(String path) {
        return path.split("\\?")[0];
    }
}
