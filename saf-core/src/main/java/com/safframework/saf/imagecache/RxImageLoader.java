package com.safframework.saf.imagecache;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.safframework.tony.common.utils.Preconditions;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class RxImageLoader {
    public static final String TAG = "RxImageLoader";

    private Context mContext = null;
    private Sources sources;

    /**
     * 初始化时 必须传Application,不能传activity的context和service的context
     * @param context
     */
    public void init(@NonNull Context context) {

        if (context instanceof Application) {
            mContext = context;
            sources = new Sources(mContext);
        } else {
            throw new IllegalArgumentException("Application needs to pass");
        }

    }

    /**
     *
     * @param url
     * @param imageView
     */
    public void displayImage(String url,ImageView imageView) {
        displayImage(url,imageView,0);
    }

    /**
     *
     * @param url
     * @param imageView
     * @param default_img_id
     */
    public void displayImage(String url, final ImageView imageView, int default_img_id) {

        // 优先加载默认图片
        if (default_img_id>0) {
            imageView.setImageResource(default_img_id);
        }

        if (Preconditions.isBlank(url)) {
            return;
        }

        ConnectableObservable<Data> connectableObservable = getObservables(url,imageView);
        connectableObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Data>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Data data) throws
                            Exception {
                        if (DataUtils.isAvailable(data)) {
                            imageView.setImageBitmap(data.bitmap);
                        }
                    }
                });
        connectableObservable.connect();
    }

    /**
     * get the observable that load img and set it to the given ImageView
     *
     * @param url the url for the img
     * @return the observable to load img
     */
    private ConnectableObservable<Data> getObservables(String url,ImageView imageView) {
        Bitmap bitmap = getBitmapFromCache(url);
        ConnectableObservable<Data> source;
        if (bitmap != null) {
            source = Observable.just(new Data(bitmap, url)).publish();
        } else {
            source = sources.getConnectableObservable(url,imageView);
        }
        return source;
    }

    /**
     *
     * @param url net url
     * @return Bitmap
     */
    private Bitmap getBitmapFromCache(String url) {
        Bitmap bm = sources.memoryCacheObservable.cache(url);
        if (bm != null) return bm;

        if (sources.diskCacheObservable!=null) {
            bm = sources.diskCacheObservable.cache(url);
        }

        if (bm != null) {
            sources.memoryCacheObservable.putData(new Data(bm,url));
            return bm;
        }

        return null;
    }

    /**
     * 清空内存中的缓存
     */
    public void clearMemCache() {
        if (sources!=null) {
            sources.memoryCacheObservable.clear();
        }
    }

    /**
     * 清空所有的缓存
     */
    public void clearAllCache() {
        if (sources!=null) {
            sources.memoryCacheObservable.clear();
            if (sources.diskCacheObservable!=null) {
                sources.diskCacheObservable.clear();
            }
        }
    }
}
