package com.test.saf.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.saf.R;
import com.test.saf.app.DemoApp;
import com.test.saf.domain.MMPicsResponse;

import java.util.List;


/**
 * Created by Tony Shen on 2016/12/1.
 */

public class ImageLoaderAdapter extends RecyclerView.Adapter<ImageLoaderAdapter.ViewHolder> {

    private List<MMPicsResponse.Pic> mList;
    private Context mContext;

    public ImageLoaderAdapter(Context context, List<MMPicsResponse.Pic> data) {
        mContext = context;
        mList = data;
    }

    @Override
    public ImageLoaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageLoaderAdapter.ViewHolder(parent, R.layout.cell_image_loader);
    }

    @Override
    public void onBindViewHolder(ImageLoaderAdapter.ViewHolder holder, int position) {
        MMPicsResponse.Pic item = (MMPicsResponse.Pic) mList.get(position);
        if (item!=null) {
            String picUrl = "http://tnfs.tngou.net/image"+item.img+"_400x400";
            DemoApp.getInstance().imageLoader.displayImage(picUrl,holder.image,R.drawable.default_girl);
            holder.title.setText(item.title);
        }
    }

    @Override
    public int getItemCount() {
        return mList!=null?mList.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        @InjectView
        ImageView image;

//        @InjectView
        TextView title;

        public ViewHolder(ViewGroup parent, @LayoutRes int resId) {
            super(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
//            Injector.injectInto(this,itemView);

            image = (ImageView)itemView.findViewById(R.id.image);
            title = (TextView)itemView.findViewById(R.id.title);
        }
    }
}
