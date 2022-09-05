package org.trado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;


public class TrieTest {
    
    public static final RouteAction<Action> stubAction = new RouteAction<Action>((req) -> TradoResponse.empty().brew().build());

    @Test
    public void test_insert(){
        var trie = new RouteTrie<RouteAction<Action>>();
        trie.insert("/hej", "GET", stubAction);
        trie.insert("/hej/hejsan/h", "POST", stubAction);
        assertNotNull(trie.action("/hej", "GET"));
        assertNotNull(trie.action("/hej/hejsan/h", "POST"));
        assertNull(trie.action("/hej/hejsan", "GET"));
    }

    @Test
    public void with_wildcards(){
        var trie = new RouteTrie<RouteAction<Action>>();
        trie.insert("/path/:param", "GET", stubAction);
        var action = trie.action("/path/hej", "GET");
        assertEquals(stubAction, action);
    }

    @Test
    public void child(){
        var trie = new RouteTrie<RouteAction<Action>>();
        trie.insert("/", "GET", stubAction);
        trie.insert("/home", "GET", stubAction);
        trie.insert("/home/error", "GET", stubAction);
        trie.insert("/home/away", "GET", stubAction);
        trie.insert("/home/away", "POST", stubAction);
    }
}