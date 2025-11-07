package com.example.ktbapi.common.auth;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireUserId {
    int paramIndex() default -1;
}
