package com.test.saf.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.saf.R;

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
    protected void bindCustomViewHolder(SAFViewHolder holder, int position) {
        ((NormalTextViewHolder)holder).name.setText((String)mList.get(position));
    }

    public class NormalTextViewHolder extends SAFViewHolder {

        @InjectView
        TextView name;

        public NormalTextViewHolder(ViewGroup parent, @LayoutRes int resId) {
            super(parent, resId);
        }
    }
}
