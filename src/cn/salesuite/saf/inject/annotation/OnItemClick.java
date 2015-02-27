package cn.salesuite.saf.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author frankswu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnItemClick {
	
	int[] id();

    /**
     * 在OnItemClick之后执行的方法名称（方法参数表Method,View,int,long），默认为空字符。
     *
     * @return
     */
    String after() default "";

    /**
     * 在OnItemClick之前执行的方法名称（方法参数表Method,View,int,long），默认为空字符。
     *
     * @return
     */
    String before() default "";

}
