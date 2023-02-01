package org.trado;

public class RouteTrie<V extends RouteAction<?>> {
    private TrieNode<String, V> root;

    public RouteTrie() {
        root = new TrieNode<>();
    }

    public void insert(String url, String identifier, V action) {
        var current = root;
        String actualKey = "";
        int pos = 0;
        for (String part : url.split("/")) {
            
            if (part.length() == 0) continue;
            pos++;
            current = current.children().computeIfAbsent(part, c -> new TrieNode<>());
            actualKey = String.copyValueOf(part.toCharArray());
        }
        
        action.wildCardPosition(pos);
        if (actualKey.length() > 0)
            action.wildCardKey(actualKey.substring(1, actualKey.length()));

        current.addMethodAction(identifier, action);
        
    }

    public int size(String url) {
        var current = findNode(url);
        return current == null ? 0 : current.size();
    }

    private TrieNode<String, V> findNode(String url) {
        var current = root;
        for (String part : url.split("/")) {
            
            if(part.length() == 0) continue;

            TrieNode<String, V> node;
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
        return current;
    }

    public V action(String url, String identifier) {
        var current = findNode(url);
        return current == null ? null : current.action(identifier);
    }


}