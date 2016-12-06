package com.test.saf.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.saf.R;
import com.test.saf.app.DemoApp;
import com.test.saf.domain.MMPicsResponse;

import java.util.List;

import cn.salesuite.saf.adapter.SAFRecyclerAdapter;
import cn.salesuite.saf.adapter.SAFViewHolder;
import cn.salesuite.saf.inject.annotation.InjectView;

/**
 * Created by Tony Shen on 2016/12/1.
 */

public class ImageLoaderAdapter extends SAFRecyclerAdapter {

    private Context mContext;

    public ImageLoaderAdapter(Context context,List<MMPicsResponse.Pic> data) {
        mContext = context;
        mList = data;
    }

    @Override
    public ImageLoaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageLoaderAdapter.ViewHolder(parent, R.layout.cell_image_loader);
    }

    @Override
    protected void bindCustomViewHolder(SAFViewHolder holder, int position) {
        MMPicsResponse.Pic item = (MMPicsResponse.Pic) mList.get(position);
        if (item!=null) {
            String picUrl = "http://tnfs.tngou.net/image"+item.img+"_400x400";
            DemoApp.getInstance().imageLoader.displayImage(picUrl,((ImageLoaderAdapter.ViewHolder)holder).image,R.drawable.default_girl);
            ((ViewHolder)holder).title.setText(item.title);
        }
    }

    public class ViewHolder extends SAFViewHolder {

        @InjectView
        ImageView image;

        @InjectView
        TextView title;

        public ViewHolder(ViewGroup parent, @LayoutRes int resId) {
            super(parent, resId);
        }
    }
}
