package com.test.saf.app;

import com.test.saf.activity.ImageDetailActivity;

import cn.salesuite.router.Router;
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

        initData();
    }

    private void initData() {

//        RouterManager.init(this);// 这一步是必须的，用于初始化Router

        Router.getInstance().setContext(mInstance);
        Router.getInstance().map("imageDetail/:image", ImageDetailActivity.class);
    }
}
