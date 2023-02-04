package org.trado;

import org.trado.controller.HttpMethod;
import org.trado.controller.Route;
import org.trado.controller.TradoController;
import org.trado.http.Method;
import org.trado.http.ContentType;

class StaticController extends TradoController {

    @HttpMethod(Method.GET)
    @Route(":fileName")
    public TradoResponse resource(TradoRequest tradoRequest){
        try {
            
            var pathArray = tradoRequest.request().uri().split("/");
            var fileName = pathArray[pathArray.length - 1];
            var bytes = getClass()
                .getClassLoader()
                .getResourceAsStream(tradoRequest.options().staticDirectory() + fileName)
                .readAllBytes();
            
            return TradoResponse.raw(bytes)
                .contentType(convertToContentType(fileName))
                .build();

        } catch (Exception e) {  
            throw new TradoException("error in reading static resource", e);
        }
    }

    private String convertToContentType(String fileName) {
        if(fileName == null)
            return ContentType.TEXT_PLAIN;

        var fileEnding = fileName.split("\\.");
        
        switch (fileEnding[fileEnding.length-1]) {
            case "js": return ContentType.APPLICATION_JAVASCRIPT;
            case "css": return ContentType.TEXT_CSS;
            default: return ContentType.TEXT_PLAIN;
        }
    }
}
