package com.safframework.aspects.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by Tony Shen on 2016/12/7.
 */
@Target({METHOD})
@Retention(CLASS)
public @interface HookMethod {

    String beforeMethod() default "";

    String afterMethod() default "";
}
