package com.safframework.prefs;

import android.content.Context;

/**
 * 配合使用@Prefs
 * Created by Tony Shen on 16/3/28.
 */
public class AppPrefs extends BasePrefs {

    private static final String PREFS_NAME = "AppPrefs";

    private AppPrefs(Context context) {
        super(context, PREFS_NAME);
    }

    public static AppPrefs get(Context context) {
        return new AppPrefs(context);
    }
}
