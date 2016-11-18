package cn.salesuite.saf.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cn.salesuite.saf.utils.SAFUtils;
import cn.salesuite.saf.utils.ToastUtils;

/**
 * Created by Tony Shen on 2016/11/17.
 */

public class SAFAppCompatActivity extends AppCompatActivity {

    public SAFApp app;
    public String TAG;
    protected Context mContext;
    protected Handler mHandler = new SafeHandler(this);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (SAFApp) this.getApplication();

        mContext = this;

        TAG = SAFUtils.makeLogTag(this.getClass());
        addActivityToManager(this);
    }

    protected  void addActivityToManager(Activity act) {
        Log.i(TAG, "addActivityToManager");
        if (!app.activityManager.contains(act)) {
            Log.i(TAG , "addActivityToManager, packagename = " + act.getClass().getName()) ;
            app.activityManager.add(act);
        }
    }

    protected void closeAllActivities() {
        Log.i(TAG, "closeAllActivities");
        for (final Activity act : app.activityManager) {
            if (act != null) {
                act.finish();
            }
        }
    }

    protected  void delActivityFromManager(Activity act) {
        Log.i(TAG,"delActivityFromManager") ;
        if (app.activityManager.contains(act)) {
            app.activityManager.remove(act);
        }
    }

    /**
     * 返回当前运行activity的名称
     * @return
     */
    protected String getCurrentActivityName() {
        int size = app.activityManager.size();
        if (size > 0) {
            return app.activityManager.get(size-1).getClass().getName();
        }
        return null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        app.imageLoader.clearMemCache();
    }

    @Override
    @TargetApi(14)
    public void onTrimMemory(int level) {
        if (SAFUtils.isICSOrHigher()) {
            super.onTrimMemory(level);

            if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
                app.imageLoader.clearMemCache();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delActivityFromManager(this);
    }

    /**
     * @param message toast的内容
     */
    protected void toast(String message) {
        ToastUtils.showShort(this, message);
    }

    /**
     * @param resId toast的内容来自String.xml
     */
    protected void toast(int resId) {
        ToastUtils.showShort(this, resId);
    }
}
