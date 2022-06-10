package org.trado;

import java.util.Optional;

import org.trado.trie.RouteTrie;

public class Routes {
    private final RouteTrie routeTrie;

    public Routes(){
        routeTrie = new RouteTrie();
    }

    public void add(String path, String method, Action action){        
        routeTrie.insert(pathWitNoParams(path), method, action);
    }

    public Optional<Action> get(String path, String method) {
        var action = routeTrie.action(pathWitNoParams(path), method);
        return action != null 
            ? Optional.of(action)
            : Optional.empty();
    }

    private static String pathWitNoParams(String path) {
        return path.split("\\?")[0];
    }
}
