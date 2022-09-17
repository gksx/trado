package org.trado;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RoutesTest {
    
    private static final Action homeGet = new Action() {
        @Override
        public TradoResponse handle(TradoRequest request) {
            return null;
        }
    };

    private static final Action homeErrorGet = new Action() {
        @Override
        public TradoResponse handle(TradoRequest request) {
            return null;
        }
    };

    private static final Action homeAwayGet = new Action() {
        @Override
        public TradoResponse handle(TradoRequest request) {
            return null;
        }
    };

    @Test
    public void test(){
        var routes = new Routes();
        routes.add("/home", "GET", homeGet);
        routes.add("/home/error", "GET", homeErrorGet);
        routes.add("/home/away", "GET", homeAwayGet);        

        assertEquals(homeGet, routes.get("/home", "GET").get().action());
        assertEquals(homeErrorGet, routes.get("/home/error", "GET").get().action());
        assertEquals(homeAwayGet, routes.get("/home/away", "GET").get().action());
    }
}
