package com.test.saf.app;

import com.safframework.router.RouterManager;
import com.safframework.saf.app.SAFApp;
import com.safframework.saf.queue.Queue;

/**
 * Created by Tony Shen on 15/11/19.
 */
public class DemoApp extends SAFApp {

    private static DemoApp mInstance = null;

    public Queue queue;

    public static DemoApp getInstance() {
        return mInstance;
    }

    public void onCreate() {

        super.onCreate();
        mInstance = this;

        initData();
    }

    private void initData() {

        RouterManager.init(this);// 这一步是必须的，用于初始化Router

        // 初始化队列
        queue = new Queue("saf_queue");
        queue.start();
    }
}
