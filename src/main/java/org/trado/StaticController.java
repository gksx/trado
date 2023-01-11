package org.trado;

import org.trado.controller.TradoController;

class StaticController extends TradoController {

    @HttpMethod("GET")
    @Route(":fileName")
    public TradoResponse resource(TradoRequest tradoRequest){
        try {
            
            var pathArray = tradoRequest.request().uri().split("/");
            var fileName = pathArray[pathArray.length - 1];
            
            var bytes = getClass().getClassLoader().getResourceAsStream("public/" + fileName).readAllBytes();
            
            return TradoResponse.raw(bytes)
                .contentType(convertToContentType(fileName))
                .build();

        } catch (Exception e) {  
            e.printStackTrace();
        }
        
        return TradoResponse.empty()
            .notFound()
            .build();
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
