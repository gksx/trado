package org.trado;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.microhttp.Request;


public class RouteTrieTest {
    
    public static final RouteAction<Action> stubAction = new RouteAction<Action>((req) -> TradoResponse.empty().brew().build());

    private static TradoRequest MOCK_REQUEST = new TradoRequest(
        new Request("", "", "", List.of(), new byte[0])
        , new TradoOptions());

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
        var response = trie.action("/home/away", "POST");
        assertEquals(response, stubAction);
    }

    @Test
    public void size() {
        var trie = new RouteTrie<RouteAction<Action>>();
        trie.insert("/home/away", "GET", stubAction);
        trie.insert("/home/away", "POST", stubAction);
        var actual = trie.size("/home/away");
        assertEquals(2, actual);
    }
}