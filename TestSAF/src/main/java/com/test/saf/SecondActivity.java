package com.test.saf;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import cn.salesuite.saf.rxjava.imagecache.Data;
import cn.salesuite.saf.rxjava.imagecache.RxImageLoader;
import rx.functions.Action1;
import rx.Subscription;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class SecondActivity extends ListActivity {

    ArrayList<String> contents = new ArrayList<>();
    RxImageLoader imageLoader = new RxImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader.init(getApplicationContext());
        for (int i = 0; i < 1; i++) {
            contents.add("http://att.x2.hiapk.com/forum/month_1008/100804175235a96c931557db2c.png");
            contents.add("http://img5.imgtn.bdimg.com/it/u=1755111051,4257519768&fm=21&gp=0.jpg");
            contents.add("http://img2.imgtn.bdimg.com/it/u=3675742480,2498904140&fm=21&gp=0.jpg");
            contents.add("http://img4.imgtn.bdimg.com/it/u=3066194656,1157598834&fm=21&gp=0.jpg");
            contents.add("http://img4.imgtn.bdimg.com/it/u=1368479866,1863058464&fm=21&gp=0.jpg");
            contents.add("http://img0.imgtn.bdimg.com/it/u=4087866388,590061000&fm=21&gp=0.jpg");
            contents.add("http://img5.imgtn.bdimg.com/it/u=1561037318,1220192463&fm=21&gp=0.jpg");
            contents.add("http://img1.imgtn.bdimg.com/it/u=662082337,2002986732&fm=21&gp=0.jpg");
            contents.add("http://img0.imgtn.bdimg.com/it/u=140615978,892488256&fm=21&gp=0.jpg");
            contents.add("http://img1.imgtn.bdimg.com/it/u=1611159065,380062274&fm=21&gp=0.jpg");
            contents.add("http://img0.imgtn.bdimg.com/it/u=4122493773,2558548513&fm=21&gp=0.jpg");
            contents.add("http://img5.imgtn.bdimg.com/it/u=4130769033,1807453428&fm=21&gp=0.jpg");
            contents.add("http://img0.imgtn.bdimg.com/it/u=2820222087,1709896027&fm=21&gp=0.jpg");
            contents.add("http://img2.imgtn.bdimg.com/it/u=3305887887,1438165635&fm=21&gp=0.jpg");
            contents.add("http://img5.imgtn.bdimg.com/it/u=4223057213,412589332&fm=21&gp=0.jpg");
            contents.add("http://img1.imgtn.bdimg.com/it/u=942005080,847944849&fm=21&gp=0.jpg");
            contents.add("http://img1.imgtn.bdimg.com/it/u=1381276545,2126755198&fm=21&gp=0.jpg");
            contents.add("http://img2.imgtn.bdimg.com/it/u=1106185903,889365061&fm=21&gp=0.jpg");
            contents.add("http://img5.imgtn.bdimg.com/it/u=3742930320,3001723047&fm=21&gp=0.jpg");
        }
        getListView().setAdapter(new RxAdapter());
    }

    class RxAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contents.size();
        }

        @Override
        public String getItem(int i) {
            return contents.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = View.inflate(SecondActivity.this, R.layout.cell_second, null);
                holder.img = (ImageView) view.findViewById(R.id.img);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            startSubscribe(holder.img, getItem(i));
            return view;
        }
    }

    class ViewHolder {
        public ImageView img;
    }

    private Subscription startSubscribe(ImageView img, String url) {
        return imageLoader.displayImageObservable(img, url,R.mipmap.ic_launcher).subscribe(new Action1<Data>() {
            @Override
            public void call(Data data) {
                Log.i("SecondActivity", "bitmap size:" + data.bitmap.getHeight() * data
                        .bitmap.getWidth());
            }
        });
    }
}
