/**
 * 
 */
package cn.salesuite.saf.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注入android的system service</br>
 * 使用方法：
 * <pre>
 * <code>
 * &#064;InjectSystemService(Context.CONNECTIVITY_SERVICE)
 * </code>
 * </pre>
 * @author Tony Shen
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectSystemService {
    String value();
}
