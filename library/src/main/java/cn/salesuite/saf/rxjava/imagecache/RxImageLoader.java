package cn.salesuite.saf.rxjava.imagecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.observables.ConnectableObservable;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class RxImageLoader {
    public static final String TAG = "RxImageLoader";

    private static Context mContext = null;
    private static Sources sources;

    public void init(Context context) {
        mContext = context;
        sources = new Sources(mContext);
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
    public void displayImage(final String url, final ImageView imageView, int default_img_id) {

        // 优先加载
        if (default_img_id>0) {
            imageView.setImageResource(default_img_id);
        }

        ConnectableObservable<Data> connectableObservable = getObservables(url,imageView);
        connectableObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Data>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(Data data) {
                    imageView.setImageBitmap(data.bitmap);
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
    private ConnectableObservable<Data> getObservables(final String url,ImageView imageView) {
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

        bm = sources.diskCacheObservable.cache(url);
        if (bm != null) return bm;

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
            sources.diskCacheObservable.clear();
        }
    }
}
