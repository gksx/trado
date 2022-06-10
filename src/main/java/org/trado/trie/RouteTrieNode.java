package org.trado.trie;

import java.util.HashMap;

import org.trado.Action;

class RouteTrieNode {
    private HashMap<String, RouteTrieNode> children;
    private HashMap<String, Action> methodActions;
    RouteTrieNode() {
        children = new HashMap<>();
        methodActions = new HashMap<>();
    }

    HashMap<String, RouteTrieNode> children(){
        return children;
    }

    void addMethodAction(String method, Action action) {
        methodActions.put(method, action);
    }

    Action action(String method) {
        return methodActions.get(method);
    }
}
