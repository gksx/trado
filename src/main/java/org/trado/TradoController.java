package org.trado;



public abstract class TradoController {

    protected static final TradoLogger logger = LogFactory.tradoLogger();

    public static TradoResponse notFound(){
        return TradoResponse.of(String.class)
            .content(errorPage)
            .statusCode(404)
            .build();
    }

    private static final String errorPage = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge">
            <meta name="viewport" content="width=<device-width>, initial-scale=1.0">
            <title>TRADO _ ERROR </title>
        </head>
        <body>
            <h1>ERROR GROWL FROM TRADO - 404</h1>
        </body>
        </html>
        """;
}
