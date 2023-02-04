package org.trado.http;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMethod {

    Method[] value() default { Method.GET};



    public enum Method {
        GET,
        POST,
        PUT,
        DELETE
    }

}
