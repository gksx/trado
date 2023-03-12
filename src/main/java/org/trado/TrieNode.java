package org.trado;

import java.util.HashMap;
import java.util.Map;

class TrieNode<K, V> {
    private final Map<K, TrieNode<K, V>> children;
    private final Map<K, V> members;
    TrieNode() {
        children = new HashMap<>();
        members = new HashMap<>();
    }

    Map<K, TrieNode<K, V>> children(){
        return children;
    }

    void member(K key, V value) {
        members.put(key, value);
    }

    V action(K key) {
        return members.get(key);
    }

    int size() {
        return members.size();
    }
}
