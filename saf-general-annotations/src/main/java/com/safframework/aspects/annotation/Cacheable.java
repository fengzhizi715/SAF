package com.safframework.aspects.annotation;

import android.annotation.TargetApi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * 由于CacheAspect需要api14以上才支持,所以同样的道理该注解也需要api14以上才支持
 * Created by Tony Shen on 16/3/23.
 */
@TargetApi(14)
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    String key();

    int expiry() default -1; // 过期时间,单位是秒
}
