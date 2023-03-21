package org.trado;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.microhttp.Header;
import org.microhttp.Request;
import org.trado.http.Cookie;
import org.trado.session.Session;

/**
 * Request wrapper for some things
 */
public class TradoRequest {
    private final Request request;
    private final Map<String, String> params; 
    private final String path;
    private final TradoOptions options;
    private final Map<String, Cookie> cookies;
    private Session session;

    public TradoRequest(Request request, TradoOptions tradoOptions){
        this.options = tradoOptions;
        this.request = request;
        params = new HashMap<>();
        this.path = request.uri().split("\\?")[0];
        queryParams();       
        this.cookies = mapHeadersToCookies(request.headers());
    }

    private Map<String, Cookie> mapHeadersToCookies(List<Header> headers) {
        var cookieHeader = headers.stream()
            .filter(h -> h.name().equalsIgnoreCase("cookie"))
            .findFirst()
            .map(Header::value);


        Map<String,Cookie> map = new HashMap<>();

        if (cookieHeader.isPresent()) {
            var arr = cookieHeader.get().split(";");

            for (String string : arr) {
                var cookie = Cookie.fromHeader(string);
                map.put(cookie.name(), cookie);
            }
        }
            
        return map;
    }

    public Optional<Cookie> cookie(String name) {
        return Optional.ofNullable(cookies.get(name));
    }


    private void queryParams() {
        try {
            if (!this.request.uri().contains("?")){
                return;
            }
            var array = this.request.uri().split("\\?")[1].split("\\&");
            for (String keyVal : array) {
                var keyValues = keyVal.split("\\=");
                var decodedKey = URLDecoder.decode(keyValues[0], StandardCharsets.UTF_8);
                var decodedValue = URLDecoder.decode(keyValues[1], StandardCharsets.UTF_8);
                params.put(decodedKey, decodedValue);
            }
        }
        catch (Exception ex) {
            //supress we dont care about malformed url params
        }
    }

    public Request request(){
        return request;
    }

    public String path(){
        return path;
    }

    TradoOptions options() {
        return options;
    }

    public Optional<String> params(String property) {
        return Optional.ofNullable(params.get(property));
    }

    public Map<String, String> params(){
        return params;
    }

    public void end() {
        throw new EndRequestException(null);
    }

    void mapRouteParams(int wildCardPosition, String wildCardKey) {
        var param = this.request.uri().split("/")[wildCardPosition];
        params.put(wildCardKey, param);
    }

    public void session(Session session) {
        this.session = session;
    }

    public Optional<Session> session() {
        return Optional.ofNullable(session);
    }
}