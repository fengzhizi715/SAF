package cn.salesuite.saf.aspects.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by Tony Shen on 16/3/23.
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    String key();

    int expiry() default -1; // 过期时间,单位是秒
}
