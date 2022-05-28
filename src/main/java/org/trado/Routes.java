package org.trado;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Routes {
    private final Map<String, Map<String, Action>> map;

    public Routes(){
        this.map = new HashMap<>();
    }

    public void add(String path, String method, Action action){
        if (map.containsKey(path)){
            var innerMap = map.get(path);
            innerMap.put(method, action);
        }
        else {
            map.put(path, new HashMap<>());
            map.get(path).put(method, action);
        }
    }

    public Optional<Action> get(String path, String method) {
        return map.containsKey(path) 
            ? Optional.of(map.get(path).getOrDefault(method, null))
            : Optional.empty();
    }
}
