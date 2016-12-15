package cn.salesuite.router.compiler;

import com.squareup.javapoet.ClassName;

/**
 * Created by Tony Shen on 2016/12/15.
 */

public class TypeUtils {

    public static final ClassName CONTEXT = ClassName.get("android.content", "Context");
    public static final ClassName ROUTER = ClassName.get("cn.salesuite.router", "Router");
    public static final ClassName ROUTER_OPTIONS = ClassName.get("cn.salesuite.router.RouterParameter", "RouterOptions");
}
