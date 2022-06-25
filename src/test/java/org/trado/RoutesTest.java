package org.trado;

import org.junit.jupiter.api.Test;

public class RoutesTest {
    
    private static final Action stubAction = null;

    @Test
    public void test(){
        var routes = new Routes();
        routes.add("/home", "GET", stubAction);
        routes.add("/home/error", "GET", stubAction);
        routes.add("/home/away", "GET", stubAction);        
    }
}
