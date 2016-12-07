package cn.salesuite.injectview.complier;

import com.squareup.javapoet.ClassName;

/**
 * Created by Tony Shen on 2016/12/7.
 */

public class TypeUtil {

    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName VIEW_BINDER = ClassName.get("cn.salesuite.injectview", "ViewBinder");
    public static final ClassName FINDER = ClassName.get("cn.salesuite.injectview.Injector", "Finder");
}
