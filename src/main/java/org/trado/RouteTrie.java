package org.trado;

class RouteTrie<V extends RouteAction<?>> {
    private final TrieNode<String, V> root;

    public RouteTrie() {
        root = new TrieNode<>();
    }

    void insert(String url, String identifier, V action) {
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
            action.wildCardKey(actualKey.substring(1));

        current.member(identifier, action);
        
    }

    int size(String url) {
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

    V action(String url, String identifier) {
        var current = findNode(url);
        return current == null ? null : current.action(identifier);
    }
}