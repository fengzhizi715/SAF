package com.test.saf.app;

import android.os.Bundle;
import com.safframework.injectview.Injector;

import cn.salesuite.saf.app.SAFAppCompatActivity;

/**
 * Created by Tony Shen on 15/11/19.
 */
public class BaseActivity extends SAFAppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Injector.injectInto(this);
    }
}
