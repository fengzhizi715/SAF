package cn.salesuite.saf.aspects.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Created by Tony Shen on 16/3/22.
 */
@Target({METHOD})
@Retention(CLASS)
public @interface Trace {
}
