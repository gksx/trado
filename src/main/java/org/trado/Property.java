package org.trado;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {

    private static Properties properties = null;

    static {
        properties = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader()
            .getResourceAsStream("application.properties");
        try {
            properties.load(is);
        } catch (IOException e) { }
        finally {
            try {
                is.close();
            } catch (IOException e) {}
        }
    }
    
    public static String get(String key) {        
        return properties.getProperty(key);
    }
}
