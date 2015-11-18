/**
 * 
 */
package cn.salesuite.saf.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * inflation  Layout的注解
 * @author Tony Shen
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InflateLayout {
	int id();
}
