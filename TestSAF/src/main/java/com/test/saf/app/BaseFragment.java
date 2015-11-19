package com.test.saf.app;

import android.os.Bundle;

import cn.salesuite.saf.app.SAFFragment;
import cn.salesuite.saf.log.L;

/**
 * Created by Tony Shen on 15/11/19.
 */
public class BaseFragment extends SAFFragment{

    protected DemoApp app;

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = DemoApp.getInstance();

        L.init(this);
    }
}
