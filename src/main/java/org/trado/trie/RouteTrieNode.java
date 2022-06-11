package org.trado.trie;

import java.util.HashMap;

class RouteTrieNode<K, V> {
    private HashMap<String, RouteTrieNode<K, V>> children;
    private HashMap<K, V> methodActions;
    RouteTrieNode() {
        children = new HashMap<>();
        methodActions = new HashMap<>();
    }

    HashMap<String, RouteTrieNode<K, V>> children(){
        return children;
    }

    void addMethodAction(K key, V value) {
        methodActions.put(key, value);
    }

    V action(K key) {
        return methodActions.get(key);
    }
}
