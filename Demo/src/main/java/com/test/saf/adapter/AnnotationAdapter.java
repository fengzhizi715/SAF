package com.test.saf.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.saf.R;
import com.test.saf.activity.AnnotationActivity;

import java.util.List;

import cn.salesuite.saf.inject.Injector;
import cn.salesuite.saf.inject.annotation.InjectView;

/**
 * Created by Tony Shen on 2016/11/22.
 */

public class AnnotationAdapter extends RecyclerView.Adapter<AnnotationAdapter.NormalTextViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<String> mList;

    public AnnotationAdapter(Context context,List<String> data) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mList = data;
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.cell_general_annotation, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        holder.name.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class NormalTextViewHolder extends RecyclerView.ViewHolder {

        @InjectView
        TextView name;

        NormalTextViewHolder(View view) {
            super(view);
            Injector.injectInto(this,view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String annotationName = mList.get(getLayoutPosition());
                    Intent i = new Intent(mContext,AnnotationActivity.class);
                    i.putExtra(AnnotationActivity.ANNO_NAME,annotationName);
                    mContext.startActivity(i);
                }
            });
        }
    }
}
