package org.trado;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {

    private static final Properties properties;

    static {
        properties = new Properties();
        try (InputStream is = ClassLoader.getSystemClassLoader()
                .getResourceAsStream("application.properties")) {
            properties.load(is);
        } catch (IOException e) {
            throw new TradoException(e);
        }
    }
    
    public static String get(String key) {        
        return properties.getProperty(key);
    }
}
