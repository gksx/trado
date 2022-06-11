package org.trado.trie;

import org.trado.Action;

public class RouteTrie {
    private RouteTrieNode root;

    public RouteTrie() {
        root = new RouteTrieNode();
    }

    public void insert(String url, String method, Action action) {
        var current = root;
        for (String part : url.split("/")) {
            if(part.length() == 0) continue;
            current = current.children().computeIfAbsent(part, c -> new RouteTrieNode());
        }
        current.addMethodAction(method, action);   
    }

    public boolean isEmpty(){
        return root.children().size() > 0;
    }

    public Action action(String url, String method) {
        var current = root;
        for (String part : url.split("/")) {
            if(part.length() == 0) continue;
            var node = current.children().get(part);
            if (node == null) {
                return null;
            }
            current = node;
        }
        return current.action(method);
    }
}