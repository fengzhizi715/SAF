package cn.salesuite.saf.rxjava.imagecache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

import cn.salesuite.saf.utils.Preconditions;
import cn.salesuite.saf.utils.SAFUtils;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class MemoryCacheObservable extends CacheObservable {


    private static final String TAG = "MemoryCacheOvservable";

    /**
     * 从内存读取数据速度是最快的，为了更大限度使用内存，这里使用了两层缓存。 硬引用缓存不会轻易被回收，用来保存常用数据，不常用的转入软引用缓存。
     */
    private static final int SOFT_CACHE_SIZE = 15; // 软引用缓存容量
    private static LruCache<String, Bitmap> mLruCache; // 硬引用缓存
    private static LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache; // 软引用缓存

    public MemoryCacheObservable() {
        int cacheSize = (int) Runtime.getRuntime().maxMemory() / 8;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                final int bitmapSize = getBitmapSize(value);
                return bitmapSize == 0 ? 1 : bitmapSize;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key,
                                        Bitmap oldValue, Bitmap newValue) {
                if(evicted){
                    if(oldValue != null && !oldValue.isRecycled()){
                        oldValue.recycle();
                    }
                } else {
                    mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
                }
            }
        };
        mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(
                SOFT_CACHE_SIZE, 0.75f, true) {
            private static final long serialVersionUID = 6040103833179403725L;

            @Override
            protected boolean removeEldestEntry(
                    Entry<String, SoftReference<Bitmap>> eldest) {
                if (size() > SOFT_CACHE_SIZE) {
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * @param value a bitmap
     * @return size of bitmap
     */
    @TargetApi(19)
    private int getBitmapSize(Bitmap value) {

        if (value == null) {
            return 0;
        }

        if (SAFUtils.isKitkatOrHigher()) {
            return value.getAllocationByteCount();
        }

        return value.getHeight() * value.getRowBytes();
    }

    /**
     * 清空lrucache和软引用缓存
     */
    public void clear() {
        mLruCache.evictAll();
        mSoftCache.clear();
    }

    /**
     * 根据key删除MemoryCache的图片缓存
     * @param url
     */
    public void remove(String url) {
        if (Preconditions.isNotBlank(url)) {
            mLruCache.remove(url);
            mSoftCache.remove(url);
        }
    }

    /**
     * Create MemoryCacheObservable
     * @param key  for Cache
     */
    public void create(final String key) {

        this.observable = Observable.create(new Observable.OnSubscribe<Data>() {
            @Override
            public void call(Subscriber<? super Data> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(new Data(mLruCache.get(key), key));
                    subscriber.onCompleted();
                }
            }
        });
    }

    @Override
    public void putData(Data data) {
        if (data!=null && data.isAvailable()) {
            synchronized (mLruCache) {
                mLruCache.put(data.url, data.bitmap);
            }
        }
    }

    @Override
    public Bitmap cache(String url) {
        return mLruCache == null ? null : mLruCache.get(url);
    }

}
