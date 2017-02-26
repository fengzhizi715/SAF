package com.safframework.permissions;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by Tony Shen on 2016/11/28.
 */

@Retention(CLASS)
@Target({METHOD})
public @interface Permission {

    String[] value();
}
