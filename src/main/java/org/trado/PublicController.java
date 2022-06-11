package org.trado;

import java.io.File;
import java.nio.file.Files;


class PublicController extends TradoController {

    @HttpMethod("GET")
    @Route(":fileName")
    public TradoResponse resource(TradoRequest tradoRequest){
        try {
            var url = getClass().getClassLoader().getResource("public");
            var pathArray = tradoRequest.request().uri().split("/");
            var fileName = pathArray[pathArray.length - 1];
            var directory = new File(url.toURI());
            File actual = null;
            for (var file : directory.listFiles()) {
                if (file.getName().equals(fileName)){
                    actual = file;
                    break;
                }
            }
            return TradoResponse.raw(Files.readAllBytes(actual.toPath()))
                .contentType(ContentType.APPLICATION_JAVASCRIPT)
                .build();

        } catch (Exception e) {  
            e.printStackTrace();
        }
        
        return TradoResponse.empty()
            .notFound()
            .build();
    }
}
