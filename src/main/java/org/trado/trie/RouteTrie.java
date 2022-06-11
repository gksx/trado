package org.trado.trie;

import org.trado.Action;

public class RouteTrie {
    private RouteTrieNode<String, Action> root;

    public RouteTrie() {
        root = new RouteTrieNode<>();
    }

    public void insert(String url, String method, Action action) {
        var current = root;
        for (String part : url.split("/")) {
            if(part.length() == 0) continue;
            if (part.startsWith(":")){
                System.out.println(part);
            }
            current = current.children().computeIfAbsent(part, c -> new RouteTrieNode<>());
        }
        current.addMethodAction(method, action);
    }

    public Action action(String url, String method) {
        var current = root;
        for (String part : url.split("/")) {
            if(part.length() == 0) continue;

            RouteTrieNode<String, Action> node;
            node = current.children().get(part);
            if (node == null) {
                var keyset = current.children().keySet();

                for (String key : keyset) {
                    if (key.startsWith(":")){
                        node = current.children().get(key);
                        break;
                    }
                }

                if (node == null){
                    return null;
                }   
            }
        
            current = node;
        }
        return current.action(method);
    }
}