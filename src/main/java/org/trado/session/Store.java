package org.trado.session;

import java.util.HashMap;
import java.util.Map;

public class Store {
    private static Map<String, Session> store = new HashMap<>();
    
    public static void add(Session session) {
        store.put(session.id(), session);
    }
}
