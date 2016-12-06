package cn.salesuite.injectview;

import android.app.Activity;

/**
 * Created by Tony Shen on 2016/12/6.
 */

public class Injector {

    public static void injectInto(Activity activity){
        String clsName = activity.getClass().getName();
        try {
            Class<?> viewBindingClass = Class.forName(clsName + "$$ViewBinder");
            ViewBinder viewBinder = (ViewBinder)viewBindingClass.newInstance();
            viewBinder.bind(activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
