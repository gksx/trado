package org.trado.trie;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.trado.TradoResponse;

public class TrieTest {
    
    @Test
    public void test_insert(){
        var trie = new RouteTrie();
        trie.insert("/hej", "GET", (req) -> TradoResponse.empty().brew().build());
        trie.insert("/hej/hejsan/h", "POST", (req) -> TradoResponse.empty().brew().build());
        assertTrue(trie.action("/hej", "GET") != null);
        assertTrue(trie.action("/hej/hejsan/h", "POST") != null);
        assertTrue(trie.action("/hej/hejsan", "GET") == null);
    }
}