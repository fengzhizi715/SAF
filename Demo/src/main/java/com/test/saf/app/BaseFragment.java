package com.test.saf.app;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import cn.salesuite.saf.app.SAFFragment;
import cn.salesuite.saf.cache.Cache;
import cn.salesuite.saf.log.L;

/**
 * Created by Tony Shen on 15/11/19.
 */
public class BaseFragment extends SAFFragment{

    protected DemoApp app;
    protected FragmentManager fmgr;
    protected Cache mCache;

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = DemoApp.getInstance();
        fmgr = getFragmentManager();

        L.init(this);
        mCache = Cache.get(mContext);
    }
}
