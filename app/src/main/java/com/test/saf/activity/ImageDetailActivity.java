package com.test.saf.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.safframework.injectview.Injector;
import com.safframework.injectview.annotations.InjectExtra;
import com.test.saf.R;
import com.test.saf.app.BaseActivity;
import com.test.saf.ui.SmoothImageView;

import cn.salesuite.saf.utils.Preconditions;

/**
 * Created by Tony Shen on 2016/12/2.
 */

public class ImageDetailActivity extends BaseActivity {

    SmoothImageView imageView;

    @InjectExtra(key="image")
    String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(0, 0, 0, 0);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setContentView(imageView);

        Injector.injectInto(this);

        if (Preconditions.isNotBlank(url)) {
            url = "http://tnfs.tngou.net/image"+url;
            app.imageLoader.displayImage(url, imageView, R.drawable.default_girl);
        }
    }
}
