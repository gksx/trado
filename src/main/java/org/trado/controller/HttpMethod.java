package org.trado.controller;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.trado.http.Method;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMethod {

    Method[] value() default { Method.GET};
    
}
