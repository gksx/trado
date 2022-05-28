package org.trado;

import java.lang.reflect.Type;
import java.util.List;

import org.microhttp.Header;
import org.microhttp.Response;

public class TradoResponse {
    private HttpStatus httpStatus = HttpStatus.OK;
    private String contentType = ContentType.TEXT_HTML;
    private Object content;
    private Type type = String.class;
    
    private TradoResponse(Type type) {
        this.type = type;
    }

    public void map() {
        
    }

    public HttpStatus httpStatus(){
        return httpStatus;
    }

    public Response toResponse(){
        var byteContent = ((String)content).getBytes();
        return new Response(
            httpStatus.code(),
            httpStatus.reason(),
            List.of(new Header("Content-Type", contentType)),
            byteContent);
    }

    public static Builder of(Type type){
        return new Builder(type);
    }

    public static class Builder{
        private TradoResponse response;
        private Builder(Type type){
            response = new TradoResponse(type);
        }

        public TradoResponse build(){
            return response;
        }

        public Builder content(Object object){
            response.content = object;
            return this;
        }

        public Builder ok(){
            response.httpStatus = HttpStatus.OK;
            return this;
        }

        public Builder badRequest(){
            response.httpStatus = HttpStatus.BAD_REQUEST;
            return this;
        }

        public Builder statusCode(int code){
            response.httpStatus = HttpStatus.byValue(code);
            return this;
        }
    }
}
