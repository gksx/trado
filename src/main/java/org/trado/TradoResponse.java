package org.trado;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.microhttp.Header;
import org.microhttp.Response;

public class TradoResponse {
    
    private HttpStatus httpStatus = HttpStatus.OK;
    private String contentType = ContentType.TEXT_HTML;
    private final Map<String, String> headers;
    private byte[] content;
    private final static String CONTENT_TYPE_HEADER = "Content-Type";
    private final static String X_POWERED_BY = "x-powered-by";

    private TradoResponse(byte[] content) {
        this.content = content;
        headers = new HashMap<>();
        headers.put(CONTENT_TYPE_HEADER, contentType);
        headers.put(X_POWERED_BY, "trado");
    }

    public HttpStatus httpStatus(){
        return httpStatus;
    }

    public static Builder raw(byte[] content){
        return new Builder(content);
    }

    public void header(String name, String value) {
        headers.put(name, value);
    }

    public static Builder content(String content) {
        return new Builder(content.getBytes());
    }

    public static Builder empty() {
        return new Builder(new byte[0]);
    }

    private List<Header> toHeaders(){
        return headers.entrySet()
            .stream()
            .map(a -> new Header(a.getKey(), a.getValue()))
            .toList();
    }

    public Response toResponse(){
        return new Response(
            httpStatus.code(),
            httpStatus.reason(),
            toHeaders(),
            content);
    }

    public static class Builder {
        private TradoResponse response;
        private Builder(byte[] content){
            response = new TradoResponse(content);
        }

        public TradoResponse build(){
            return response;
        }

        public Builder ok(){
            response.httpStatus = HttpStatus.OK;
            return this;
        }

        public Builder header(String name, String value){
            response.headers.put(name, value);
            return this;
        }

        public Builder badRequest(){
            response.httpStatus = HttpStatus.BAD_REQUEST;
            return this;
        }

        public Builder brew(){
            response.httpStatus = HttpStatus.IM_A_TEAPOT;
            return this;
        }

        public Builder statusCode(int code){
            response.httpStatus = HttpStatus.byValue(code);
            return this;
        }

        public Builder contentType(String contentType) {
            response.headers.put(CONTENT_TYPE_HEADER, contentType);
            return this;
        }

        public Builder notFound() {
            response.httpStatus = HttpStatus.NOT_FOUND;
            return this;
        }
    }
}