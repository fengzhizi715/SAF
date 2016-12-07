package cn.salesuite.injectview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;

/**
 * Created by Tony Shen on 2016/12/6.
 */

public class Injector {

    public enum Finder {
        DIALOG {
            @Override
            public View findById(Object source, int id) {
                return ((Dialog) source).findViewById(id);
            }
        },
        ACTIVITY {
            @Override
            public View findById(Object source, int id) {
                return ((Activity) source).findViewById(id);
            }
        },
        FRAGMENT {
            @Override
            public View findById(Object source, int id) {
                return ((View) source).findViewById(id);
            }
        },
        VIEW {
            @Override
            public View findById(Object source, int id) {
                return ((View) source).findViewById(id);
            }
        },
        VIEW_HOLDER {
            @Override
            public View findById(Object source, int id) {
                return ((View) source).findViewById(id);
            }
        };

        public abstract View findById(Object source, int id);
    }


    public static void injectInto(Activity activity){
        inject(activity, activity);
    }

    private static void inject(Context context, Object target) {
        String className = context.getClass().getName();
        try {
            Class<?> finderClass = Class.forName(className + "$$ViewBinder");
            ViewBinder viewBinder = (ViewBinder) finderClass.newInstance();
            viewBinder.inject(context, target, Finder.ACTIVITY);
        } catch (Exception e) {
            throw new RuntimeException("Unable to inject for " + className, e);
        }
    }
}
