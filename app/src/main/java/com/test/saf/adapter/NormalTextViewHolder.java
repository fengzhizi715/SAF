package com.test.saf.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectView;
import com.test.saf.R;


/**
 * Created by Tony Shen on 2016/12/24.
 */

public class NormalTextViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.name)
    TextView name;

    public NormalTextViewHolder(View itemView) {
        super(itemView);
        Injector.injectInto(this,itemView);
    }
}
