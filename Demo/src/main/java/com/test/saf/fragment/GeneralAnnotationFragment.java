package com.test.saf.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.test.saf.R;
import com.test.saf.adapter.AnnoAdapter;
import com.test.saf.app.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.salesuite.saf.inject.Injector;
import cn.salesuite.saf.inject.annotation.InjectView;

/**
 * Created by tony on 2016/11/20.
 */

public class GeneralAnnotationFragment extends BaseFragment {

    @InjectView
    ListView listview;

    AnnoAdapter adapter;

    List<String> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_general_annotation, container, false);
        Injector.injectInto(this, v);

        initData();

        return v;
    }

    private void initData() {

        data = new ArrayList<String>();
        data.add("@Async");
        data.add("@Cacheable");
        data.add("@LogMethod");
        data.add("@Prefs");
        data.add("@Safe");
        data.add("@Trace");
        adapter = new AnnoAdapter(mContext,data);
        listview.setAdapter(adapter);
        listview.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

            }
        });
    }
}
