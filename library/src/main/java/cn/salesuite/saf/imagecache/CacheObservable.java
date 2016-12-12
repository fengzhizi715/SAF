package cn.salesuite.saf.imagecache;

import android.graphics.Bitmap;

import rx.Observable;

/**
 * Created by Tony Shen on 15/11/13.
 */
public abstract class CacheObservable {

    public Observable<Data> observable;

    public abstract void putData(Data data);

    public abstract Bitmap cache(String info);
}
