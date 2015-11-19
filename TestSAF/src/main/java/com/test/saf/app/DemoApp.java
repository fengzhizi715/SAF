package com.test.saf.app;

import cn.salesuite.saf.app.SAFApp;

/**
 * Created by Tony Shen on 15/11/19.
 */
public class DemoApp extends SAFApp {

    private static DemoApp mInstance = null;

    public static DemoApp getInstance() {
        return mInstance;
    }

    public void onCreate() {

        super.onCreate();
        mInstance = this;
    }
}
