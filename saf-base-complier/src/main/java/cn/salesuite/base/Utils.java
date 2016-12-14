package cn.salesuite.base;

import javax.lang.model.element.TypeElement;

import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by Tony Shen on 2016/12/14.
 */

public class Utils {

    /**
     * 判断类描述符是否是public的
     *
     * @param annotatedClass 需要判断的类
     * @return 如果是public的返回true，其他返回false
     */
    public static boolean isPublic(TypeElement annotatedClass) {
        return annotatedClass.getModifiers().contains(PUBLIC);
    }

    /**
     * 判断类描述符是否是abstract的
     *
     * @param annotatedClass 需要判断的类
     * @return 如果是abstract的返回true，其他返回false
     */
    public static boolean isAbstract(TypeElement annotatedClass) {
        return annotatedClass.getModifiers().contains(ABSTRACT);
    }
}
