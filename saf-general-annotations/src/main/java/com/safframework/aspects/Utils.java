package com.safframework.aspects;

import android.annotation.TargetApi;
import android.content.Context;

import com.safframwork.tony.common.reflect.Reflect;

/**
 * Created by Tony Shen on 2017/2/7.
 */

public class Utils {

    /**
     * 获取全局的context，也就是Application Context
     * @return
     */
    @TargetApi(14)
    public static Context getContext() {
        return Reflect.on("android.app.ActivityThread").call("currentApplication").get();
    }
}
