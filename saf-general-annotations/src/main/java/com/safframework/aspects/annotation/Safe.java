package com.safframework.aspects.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by Tony Shen on 16/3/23.
 */
@Target({METHOD})
@Retention(CLASS)
public @interface Safe {
}
