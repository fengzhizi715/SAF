package com.test.saf.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.test.saf.R;
import com.test.saf.app.BaseFragment;

import cn.salesuite.saf.inject.Injector;
import cn.salesuite.saf.inject.annotation.InjectView;

/**
 * Created by Tony Shen on 2016/11/22.
 */

public class ImageLoaderFragment extends BaseFragment {

    @InjectView(id=R.id.menu_labels_right)
    FloatingActionMenu faMenu;

    @InjectView
    FloatingActionButton fab1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_imageloader, container, false);
        Injector.injectInto(this, v);

        return v;
    }
}
