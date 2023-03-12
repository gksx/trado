package org.trado.http;

public record Cookie(String name, String value) {
    public static Cookie fromHeader(String value) {
        var s = value.split("=");
        return new Cookie(s[0], s[1]);
    }
}
