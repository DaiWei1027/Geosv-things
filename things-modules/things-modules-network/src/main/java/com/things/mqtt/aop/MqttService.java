package com.things.mqtt.aop;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MqttService {

    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
}

