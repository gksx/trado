package org.trado;

import java.util.HashMap;
import java.util.Map;

class TrieNode<K, V> {
    private final Map<String, TrieNode<K, V>> children;
    private final Map<K, V> methodActions;
    TrieNode() {
        children = new HashMap<>();
        methodActions = new HashMap<>();
    }

    Map<String, TrieNode<K, V>> children(){
        return children;
    }

    void addMethodAction(K key, V value) {
        methodActions.put(key, value);
    }

    V action(K key) {
        return methodActions.get(key);
    }

    int size() {
        return methodActions.size();
    }
}
