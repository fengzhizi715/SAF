package cn.salesuite.saf.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.salesuite.saf.inject.Injector;

/**
 * Created by Tony Shen on 16/2/2.
 */
public abstract class Presenter<T> extends RecyclerView.ViewHolder {

    public Presenter(View convertView) {
        super(convertView);
        Injector.injectInto(this, convertView);
    }

    public abstract void onBind(int position, T item);
}
