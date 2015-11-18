package cn.salesuite.saf.rxjava.imagecache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class Data {

    public Bitmap bitmap;
    public String url;
    private boolean isAvailable;

    public Data(Bitmap bitmap, String url) {
        this.bitmap = bitmap;
        this.url = url;
    }

    public Data(File f, String url) {
        if (f != null && f.exists()) {
            this.url = url;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isAvailable() {
        isAvailable = url != null && bitmap != null;
        return isAvailable;
    }
}
