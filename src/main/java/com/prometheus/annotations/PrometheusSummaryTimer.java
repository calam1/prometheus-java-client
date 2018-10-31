package com.prometheus.annotations;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@InterceptorBinding
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface PrometheusSummaryTimer {
    @Nonbinding String name();
    @Nonbinding String help();
    @Nonbinding String [] labelNames() default {};
    @Nonbinding String [] labels() default {};
}

