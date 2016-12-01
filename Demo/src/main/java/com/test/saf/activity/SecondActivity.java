package com.test.saf.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.test.saf.R;
import com.test.saf.app.DemoApp;

import java.util.ArrayList;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class SecondActivity extends ListActivity {

    ArrayList<String> contents = new ArrayList<>();
    private ViewHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contents.add("http://petapixel.com/assets/uploads/2013/04/waterski2.webp");
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
        contents.add("http://ww3.sinaimg.cn/large/610dc034jw1f71bezmt3tj20u00k0757.jpg");
        contents.add("http://ww4.sinaimg.cn/large/610dc034jw1f76axy6xcsj20u00yqq49.jpg");
        contents.add("http://ww3.sinaimg.cn/large/610dc034jw1f6xsqw8057j20dw0kugpf.jpg");
        contents.add("http://ww2.sinaimg.cn/large/610dc034jw1f6vyy5a99ej20u011gq87.jpg");
        contents.add("http://ww4.sinaimg.cn/large/610dc034jw1f6uv5gbsa9j20u00qxjt6.jpg");
        contents.add("http://ww1.sinaimg.cn/large/610dc034jw1f6u4boc0k2j20u00u0gni.jpg");
        contents.add("http://ww3.sinaimg.cn/large/610dc034jw1f6qsn74e3yj20u011htc6.jpg");
        contents.add("http://ww2.sinaimg.cn/large/610dc034jw1f6pnw6i7lqj20u00u0tbr.jpg");
        contents.add("http://ww2.sinaimg.cn/large/610dc034jw1f6ofd28kr6j20dw0kudgx.jpg");
        contents.add("http://ww1.sinaimg.cn/large/610dc034jw1f6nbm78pplj20dw0i2djy.jpg");
        contents.add("http://ww3.sinaimg.cn/large/610dc034jw1f71bezmt3tj20u00k0757.jpg");
        contents.add("http://ww4.sinaimg.cn/large/610dc034jw1f76axy6xcsj20u00yqq49.jpg");
        contents.add("http://ww3.sinaimg.cn/large/610dc034jw1f6xsqw8057j20dw0kugpf.jpg");
        contents.add("http://ww2.sinaimg.cn/large/610dc034jw1f6vyy5a99ej20u011gq87.jpg");
        contents.add("http://ww4.sinaimg.cn/large/610dc034jw1f6uv5gbsa9j20u00qxjt6.jpg");
        contents.add("http://ww1.sinaimg.cn/large/610dc034jw1f6u4boc0k2j20u00u0gni.jpg");
        contents.add("http://ww3.sinaimg.cn/large/610dc034jw1f6qsn74e3yj20u011htc6.jpg");
        contents.add("http://ww2.sinaimg.cn/large/610dc034jw1f6pnw6i7lqj20u00u0tbr.jpg");
        contents.add("http://ww2.sinaimg.cn/large/610dc034jw1f6ofd28kr6j20dw0kudgx.jpg");
        contents.add("http://ww1.sinaimg.cn/large/610dc034jw1f6nbm78pplj20dw0i2djy.jpg");
        contents.add("http://ww3.sinaimg.cn/large/610dc034jw1f71bezmt3tj20u00k0757.jpg");
        contents.add("http://ww4.sinaimg.cn/large/610dc034jw1f76axy6xcsj20u00yqq49.jpg");
        contents.add("http://ww3.sinaimg.cn/large/610dc034jw1f6xsqw8057j20dw0kugpf.jpg");
        contents.add("http://ww2.sinaimg.cn/large/610dc034jw1f6vyy5a99ej20u011gq87.jpg");
        contents.add("http://ww4.sinaimg.cn/large/610dc034jw1f6uv5gbsa9j20u00qxjt6.jpg");
        contents.add("http://ww1.sinaimg.cn/large/610dc034jw1f6u4boc0k2j20u00u0gni.jpg");
        contents.add("http://ww3.sinaimg.cn/large/610dc034jw1f6qsn74e3yj20u011htc6.jpg");
        contents.add("http://ww2.sinaimg.cn/large/610dc034jw1f6pnw6i7lqj20u00u0tbr.jpg");
        contents.add("http://ww2.sinaimg.cn/large/610dc034jw1f6ofd28kr6j20dw0kudgx.jpg");
        contents.add("http://ww1.sinaimg.cn/large/610dc034jw1f6nbm78pplj20dw0i2djy.jpg");
        contents.add("http://ww3.sinaimg.cn/large/610dc034jw1f71bezmt3tj20u00k0757.jpg");
        contents.add("http://ww4.sinaimg.cn/large/610dc034jw1f76axy6xcsj20u00yqq49.jpg");
        contents.add("http://ww3.sinaimg.cn/large/610dc034jw1f6xsqw8057j20dw0kugpf.jpg");
        contents.add("http://ww2.sinaimg.cn/large/610dc034jw1f6vyy5a99ej20u011gq87.jpg");
        contents.add("http://ww4.sinaimg.cn/large/610dc034jw1f6uv5gbsa9j20u00qxjt6.jpg");
        contents.add("http://ww1.sinaimg.cn/large/610dc034jw1f6u4boc0k2j20u00u0gni.jpg");
        contents.add("http://ww3.sinaimg.cn/large/610dc034jw1f6qsn74e3yj20u011htc6.jpg");
        contents.add("http://ww2.sinaimg.cn/large/610dc034jw1f6pnw6i7lqj20u00u0tbr.jpg");
        contents.add("http://ww2.sinaimg.cn/large/610dc034jw1f6ofd28kr6j20dw0kudgx.jpg");
        contents.add("http://ww1.sinaimg.cn/large/610dc034jw1f6nbm78pplj20dw0i2djy.jpg");

//        contents.add("http://7xql2z.com2.z0.glb.qiniucdn.com/tour_list_01.png");
//        contents.add("http://7xql2z.com2.z0.glb.qiniucdn.com/tour_list_02.png");
//        contents.add("http://7xql2z.com2.z0.glb.qiniucdn.com/tour_list_03.png");
//        contents.add("http://7xql2z.com2.z0.glb.qiniucdn.com/tour_list_04.png");
//        contents.add("http://7xql2z.com2.z0.glb.qiniucdn.com/tour_list_05.png");
//        contents.add("http://7xql2z.com2.z0.glb.qiniucdn.com/tour_list_06.png");
//        contents.add("http://7xql2z.com2.z0.glb.qiniucdn.com/tour_list_07.png");
//        contents.add("http://7xql2z.com2.z0.glb.qiniucdn.com/tour_list_08.png");
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

            if (view == null) {
                holder = new ViewHolder();
                view = View.inflate(SecondActivity.this, R.layout.cell_second, null);
                holder.img = (ImageView) view.findViewById(R.id.img);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            DemoApp.getInstance().imageLoader.displayImage(getItem(i), holder.img, R.mipmap
                    .ic_launcher);
            return view;
        }
    }

    class ViewHolder {
        public ImageView img;
    }
}
