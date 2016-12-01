package cn.salesuite.saf.rxjava.imagecache;

import android.graphics.Bitmap;

/**
 * 封装Bitmap的Data对象
 * Created by Tony Shen on 15/11/13.
 */
public class Data {

    public Bitmap bitmap;
    public String url;

    public Data(Bitmap bitmap, String url) {
        this.bitmap = bitmap;
        this.url = url;
    }

    public boolean isAvailable() {

        return url != null && bitmap != null && !bitmap.isRecycled();
    }
}
