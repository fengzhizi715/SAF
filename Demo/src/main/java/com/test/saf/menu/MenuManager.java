package com.test.saf.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by tony on 2016/11/20.
 */

public class MenuManager {

    private static MenuManager instance = null;
    private FragmentManager fragmentManager;
    private MenuType curType;

    public enum MenuType {

        HOME("首页",false);

        public final String title;
        public final boolean removed;

        MenuType(String title,boolean removed) {
            this.title = title;
            this.removed = removed;
        }

        public String getTitle() {
            return title;
        }

        public boolean isRemoved() {
            return removed;
        }
    }

    private MenuManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        curType = MenuType.HOME;
    }

    public static MenuManager getInstance(FragmentManager fragmentManager) {
        if (instance == null) {
            instance = new MenuManager(fragmentManager);
        }

        return instance;
    }

    public MenuType getCurType() {

        return curType;
    }


}
