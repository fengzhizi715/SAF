package com.test.saf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.test.saf.R;
import com.test.saf.activity.AnnotationActivity;
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

    List<String> data = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_general_annotation, container, false);
        Injector.injectInto(this, v);

        initData();

        return v;
    }

    private void initData() {

        data.clear();
        data.add("@Async");
        data.add("@Cacheable");
        data.add("@LogMethod");
        data.add("@Prefs");
        data.add("@Safe");
        data.add("@Trace");
        adapter = new AnnoAdapter(mContext,data);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String annotationName = data.get(position);

                Intent i = new Intent(mContext, AnnotationActivity.class);
                i.putExtra(AnnotationActivity.ANNO_NAME,annotationName);
                startActivity(i);
            }
        });
    }
}
