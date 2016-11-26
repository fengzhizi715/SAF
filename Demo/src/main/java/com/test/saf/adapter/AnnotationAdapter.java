package com.test.saf.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.saf.R;
import com.test.saf.activity.AnnotationActivity;

import java.util.List;

import cn.salesuite.saf.adapter.SAFRecyclerAdapter;
import cn.salesuite.saf.adapter.SAFViewHolder;
import cn.salesuite.saf.inject.annotation.InjectView;

/**
 * Created by Tony Shen on 2016/11/22.
 */

public class AnnotationAdapter extends SAFRecyclerAdapter {

    private Context mContext;

    public AnnotationAdapter(Context context,List<String> data) {
        mContext = context;
        mList = data;
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(parent, R.layout.cell_general_annotation);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((NormalTextViewHolder)holder).name.setText((String)mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class NormalTextViewHolder extends SAFViewHolder {

        @InjectView
        TextView name;

        public NormalTextViewHolder(ViewGroup parent, @LayoutRes int resId) {
            super(parent, resId);
            getView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String annotationName = (String) mList.get(getLayoutPosition());
                    Intent i = new Intent(mContext,AnnotationActivity.class);
                    i.putExtra(AnnotationActivity.ANNO_NAME,annotationName);
                    mContext.startActivity(i);
                }
            });
        }
    }
}
