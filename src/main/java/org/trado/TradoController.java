package org.trado;

public abstract class TradoController {

    protected static TradoResponse notFound(){
        return TradoResponse.content(errorPage(404))
            .statusCode(404)
            .build();
    }

    protected static TradoResponse internalError(){
        return TradoResponse.content(errorPage(500))
            .statusCode(500)
            .build();
    }

    private static final String errorPage(int statusCode){
        return String.format("""
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=<device-width>, initial-scale=1.0">
            <title>TRADO _ ERROR </title>
        </head>
        <body>
            <h1>ERROR GROWL FROM TRADO - %d</h1>
        </body>
        </html>
        """, statusCode);
    }
}
