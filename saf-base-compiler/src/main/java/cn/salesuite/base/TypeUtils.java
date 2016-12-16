package cn.salesuite.base;

import com.squareup.javapoet.ClassName;

/**
 * Created by Tony Shen on 2016/12/16.
 */

public class TypeUtils {

    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName VIEW_BINDER = ClassName.get("cn.salesuite.injectview", "ViewBinder");
    public static final ClassName FINDER = ClassName.get("cn.salesuite.injectview.Injector", "Finder");
    public static final ClassName ARRAY_LIST = ClassName.get("java.util", "ArrayList");
    public static final ClassName CONTEXT = ClassName.get("android.content", "Context");
    public static final ClassName ROUTER = ClassName.get("cn.salesuite.router", "Router");
    public static final ClassName ROUTER_OPTIONS = ClassName.get("cn.salesuite.router.RouterParameter", "RouterOptions");
}
