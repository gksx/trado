package org.trado.trie;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.trado.Action;
import org.trado.TradoResponse;

public class TrieTest {
    
    static final Action stubAction = (req) -> TradoResponse.empty().brew().build();

    @Test
    public void test_insert(){
        var trie = new RouteTrie();
        trie.insert("/hej", "GET", stubAction);
        trie.insert("/hej/hejsan/h", "POST", stubAction);
        assertNotNull(trie.action("/hej", "GET"));
        assertNotNull(trie.action("/hej/hejsan/h", "POST"));
        assertNull(trie.action("/hej/hejsan", "GET"));
    }

    @Test
    public void with_wildcards(){
        var trie = new RouteTrie();
        trie.insert("/path/:param", "GET", stubAction);
        var action = trie.action("/path/hej", "GET");
        assertNotNull(action);
    }
}