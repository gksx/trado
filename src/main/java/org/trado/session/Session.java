package org.trado.session;

public record Session(String id, long ttl, boolean authenticated) {
    
    byte[] cookie() {
        return id.getBytes();
    }
}