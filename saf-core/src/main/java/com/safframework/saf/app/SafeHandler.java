package com.safframework.saf.app;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Tony Shen on 16/7/5.
 */
public class SafeHandler extends Handler {

    private final WeakReference<Activity> mActivity;

    public SafeHandler(Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        if(mActivity.get() == null) {
            return;
        }
    }
}
