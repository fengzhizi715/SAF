package cn.salesuite.saf.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.salesuite.saf.inject.Injector;

/**
 * Created by Tony Shen on 2016/11/24.
 */

public class SAFViewHolder extends RecyclerView.ViewHolder {

    public SAFViewHolder(ViewGroup parent, @LayoutRes int resId) {
        super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
        Injector.injectInto(this,itemView);
    }

    public SAFViewHolder(View view) {
        super(view);
        Injector.injectInto(this,view);
    }

    /**
     * 获取Context实例
     * @return context
     */
    protected Context getContext() {
        return itemView.getContext();
    }

    protected View getView() {
        return itemView;
    }
}
