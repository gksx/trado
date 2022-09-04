package org.trado.trie;

public class RouteTrie<V> {
    private TrieNode<String, V> root;

    public RouteTrie() {
        root = new TrieNode<>();
    }

    public void insert(String url, String identifier, V action) {
        var current = root;
        for (String part : url.split("/")) {
            
            if (part.length() == 0) continue;
            
            current = current.children().computeIfAbsent(part, c -> new TrieNode<>());
        }
        current.addMethodAction(identifier, action);
    }

    public V action(String url, String identifier) {
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
        return current.action(identifier);
    }
}