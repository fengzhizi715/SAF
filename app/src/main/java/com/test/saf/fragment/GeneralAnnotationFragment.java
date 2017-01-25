package com.test.saf.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.router.Router;
import com.test.saf.R;
import com.test.saf.adapter.AnnotationAdapter;
import com.test.saf.app.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.salesuite.saf.permissions.PermissionGuard;
import cn.salesuite.saf.recyclerview.OnItemClickListener;

/**
 * Created by tony on 2016/11/20.
 */

public class GeneralAnnotationFragment extends BaseFragment {

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;

    List<String> data = new ArrayList<String>();

    private PermissionGuard permissionGuard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_general_annotation, container, false);
        Injector.injectInto(this, v);
        permissionGuard = new PermissionGuard(mContext,this);
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

        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));//这里用线性显示 类似于listview
        recyclerview.setAdapter(new AnnotationAdapter(mContext,data));
        recyclerview.getLayoutManager().setAutoMeasureEnabled(true);
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setHasFixedSize(false);
        recyclerview.addOnItemTouchListener(new OnItemClickListener(recyclerview){

            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                String annotationName = (String) data.get(position);
                Router.getInstance().open("annotationName/"+annotationName);
            }

            @Override
            public void onLongPress(RecyclerView.ViewHolder holder, int position) {
            }
        });
        recyclerview.addItemDecoration(new DividerItemDecoration(
                mContext, DividerItemDecoration.VERTICAL));
    }
}
