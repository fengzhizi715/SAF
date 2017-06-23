package com.test.saf.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safframework.injectview.Injector;
import com.test.saf.R;
import com.test.saf.app.BaseFragment;

/**
 * Created by Tony Shen on 2017/6/23.
 */

public class PermissionFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_permission, container, false);
        Injector.injectInto(this, v);

        initViews();

        return v;
    }

    private void initViews() {
    }
}
