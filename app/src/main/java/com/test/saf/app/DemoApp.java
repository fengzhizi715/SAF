package com.test.saf.app;

import cn.salesuite.saf.app.SAFApp;
import cn.salesuite.saf.route.Router;

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

        initData();
    }

    private void initData() {

        Router.getInstance().setContext(getApplicationContext()); // 这一步是必须的，用于初始化Router
    }
}
