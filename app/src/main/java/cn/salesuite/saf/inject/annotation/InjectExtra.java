/**
 * 
 */
package cn.salesuite.saf.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注入android的extra</br>
 * 使用方法：
 * <pre>
 * <code>
 * &#064;InjectExtra(key="keyname")
 * </code>
 * </pre>
 * @author Tony Shen
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectExtra {
	
	String key() default "";
	
	int defaultInt() default 0;
	
	boolean defaultBoolean() default false;
	
    String defaultString() default "";

    double defaultDouble() default 0.0;

    long defaultLong() default 0L;
    
}
