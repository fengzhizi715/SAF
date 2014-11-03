/**
 * 
 */
package cn.salesuite.saf.http.rest.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Tony Shen
 *
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface POST {
	String value();
}
