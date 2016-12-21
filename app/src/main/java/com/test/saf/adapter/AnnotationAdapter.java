package com.test.saf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.saf.R;

import java.util.List;

/**
 * Created by Tony Shen on 2016/11/22.
 */

public class AnnotationAdapter extends RecyclerView.Adapter<AnnotationAdapter.NormalTextViewHolder> {

    private List<String> mList;
    private Context mContext;

    public AnnotationAdapter(Context context,List<String> data) {
        mContext = context;
        mList = data;
    }

    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_general_annotation, parent, false);
        return new NormalTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        holder.name.setText((String)mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList!=null?mList.size():0;
    }

    public class NormalTextViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public NormalTextViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }


}
