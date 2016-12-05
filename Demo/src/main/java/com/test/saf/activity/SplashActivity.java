package com.test.saf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.test.saf.R;
import com.test.saf.app.BaseActivity;
import com.test.saf.config.Config;

import java.util.concurrent.TimeUnit;

import cn.salesuite.saf.log.L;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Tony Shen on 2016/12/5.
 */

public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
    }

    private void initData() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）
        float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5 /2.0）
        L.i("device screen: " + width + "*" + height + " desity: " + density);
        Config.height = height;
        Config.width = width;
        Config.density = density;

        loadingNext();
    }

    /**
     * 跳转到主页面
     */
    private void loadingNext() {

        // 延迟2秒跳到首页
        Observable.timer(2000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Intent i = new Intent(mContext, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                });
    }
}