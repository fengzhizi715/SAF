package cn.salesuite.injectview.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Tony Shen on 2016/12/8.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface InjectExtra {

    String key() default "";

//    int defaultInt() default 0;
//
//    boolean defaultBoolean() default false;
//
//    String defaultString() default "";
//
//    double defaultDouble() default 0.0;
//
//    long defaultLong() default 0L;
}
