package org.trado;

import java.util.List;

import org.microhttp.Header;
import org.microhttp.Response;

public class TradoResponse {
    private HttpStatus httpStatus = HttpStatus.OK;
    private String contentType = ContentType.TEXT_HTML;
    private byte[] content;
    private TradoResponse(byte[] content) {
        this.content = content;
    }

    public HttpStatus httpStatus(){
        return httpStatus;
    }

    public static Builder raw(byte[] content){
        return new Builder(content);
    }

    public static Builder content(String content) {
        return new Builder(content.getBytes());
    }

    public Response toResponse(){
        return new Response(
            httpStatus.code(),
            httpStatus.reason(),
            List.of(new Header("Content-Type", contentType)),
            content);
    }

    public static class Builder{
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
