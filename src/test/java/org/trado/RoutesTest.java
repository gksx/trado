package org.trado;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RoutesTest {
    
    private static final Action homeGet = request -> null;

    private static final Action homeErrorGet = request -> null;

    private static final Action homeAwayGet = request -> null;

    @Test
    public void test(){
        var routes = new Routes();
        routes.add("/home", "GET", homeGet);
        routes.add("/home/error", "GET", homeErrorGet);
        routes.add("/home/away", "GET", homeAwayGet);        

        assertEquals(homeGet, routes.get("/home", "GET").orElseThrow().action());
        assertEquals(homeErrorGet, routes.get("/home/error", "GET").orElseThrow().action());
        assertEquals(homeAwayGet, routes.get("/home/away", "GET").orElseThrow().action());
    }
}
