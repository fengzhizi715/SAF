package com.test.saf.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test.saf.R;

import java.util.List;

import cn.salesuite.saf.adapter.SAFAdapter;
import cn.salesuite.saf.inject.annotation.InjectView;
import cn.salesuite.saf.log.L;

/**
 * Created by tony on 2016/11/20.
 */

public class AnnoAdapter extends SAFAdapter<String> {

    private LayoutInflater mInflater;
    private ViewHolder holder;

    public AnnoAdapter(Context context,List<String> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cell_general_annotation, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        String item = mList.get(position);
        holder.name.setText(item);

        return convertView;
    }

    class ViewHolder extends SAFViewHolder{

        @InjectView
        public TextView name;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
