package cn.salesuite.saf.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注入android的组件，包括基本类型的组件和自定义组件数组
 * 
 * @author frankswu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectViews {
	
	int[] ids();

}
