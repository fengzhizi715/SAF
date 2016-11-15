package com.test.saf.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import cn.salesuite.saf.app.SAFActivity;
import cn.salesuite.saf.eventbus.EventBus;
import cn.salesuite.saf.inject.Injector;
import cn.salesuite.saf.log.L;

/**
 * Created by Tony Shen on 15/11/19.
 */
public class BaseActivity extends SAFActivity {

    protected DemoApp app;
    protected EventBus eventBus;
    protected Dialog mDialog;
    protected Context mContext;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        app = DemoApp.getInstance();

        L.init(this);
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Injector.injectInto(this);
    }
}
