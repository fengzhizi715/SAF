package cn.salesuite.saf.rxjava.imagecache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cn.salesuite.saf.utils.Preconditions;
import cn.salesuite.saf.utils.SAFUtils;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class MemoryCacheObservable extends CacheObservable {


    private static final String TAG = "MemoryCacheOvservable";

    private static LruCache<String, Bitmap> mLruCache; // 硬引用缓存
    private Set<SoftReference<Bitmap>> mReusableBitmaps;

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
                        oldValue = null;
                    }
                } else {
                    mReusableBitmaps.add(new SoftReference<>(oldValue));
                }
            }
        };

        mReusableBitmaps = Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
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
     * This method iterates through the reusable bitmaps, looking for one
     * to use for inBitmap:
     */
    public Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {

        Bitmap bitmap = null;

        if (mReusableBitmaps != null && !mReusableBitmaps.isEmpty()) {
            synchronized (this) {
                final Iterator<SoftReference<Bitmap>> iterator = mReusableBitmaps.iterator();
                Bitmap item;

                while (iterator.hasNext()) {
                    item = iterator.next().get();

                    if (item!=null && item.isMutable()) {
                        if (canUseForInBitmap(item, options)) {
                            bitmap = item;
                            // 从reusable set中移除，避免再次被使用
                            iterator.remove();
                            Log.i(TAG, "Find reusable bitmap");
                            break;
                        }
                    } else {
                        iterator.remove();
                    }
                }
            }
        }
        return bitmap;
    }

    private boolean canUseForInBitmap(Bitmap candidate, BitmapFactory.Options targetOptions) {

        if (SAFUtils.isKitkatOrHigher()) {

            // From Android 4.4 (KitKat) onward we can re-use if the byte size of
            // the new bitmap is smaller than the reusable bitmap candidate
            // allocation byte count.
            final int width = targetOptions.outWidth / targetOptions.inSampleSize;
            final int height = targetOptions.outHeight / targetOptions.inSampleSize;

            final int byteCount = width * height * getBytesPerPixel(candidate.getConfig());
            return byteCount <= candidate.getAllocationByteCount();
        } else {

            return candidate.getWidth() == targetOptions.outWidth &&
                    candidate.getHeight() == targetOptions.outHeight && targetOptions.inSampleSize == 1;
        }
    }

    private int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        } else if (config == Bitmap.Config.RGB_565) {
            return 2;
        } else if (config == Bitmap.Config.ARGB_4444) {
            return 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

    /**
     * 清空lrucache和软引用缓存
     */
    public void clear() {
        mLruCache.evictAll();
        clearReusableBitmaps();
    }

    private void clearReusableBitmaps() {
        if (mReusableBitmaps != null && !mReusableBitmaps.isEmpty()) {
            synchronized (MemoryCacheObservable.class) {
                final Iterator<SoftReference<Bitmap>> iterator = mReusableBitmaps.iterator();

                Bitmap item = null;
                while (iterator.hasNext()) {
                    item = iterator.next().get();

                    if (item!=null && !item.isRecycled()) {
                        item = null;
                    }
                }
                mReusableBitmaps.clear();
            }
        }
    }

    /**
     * 根据key删除MemoryCache的图片缓存
     * @param url
     */
    public void remove(String url) {
        if (Preconditions.isNotBlank(url)) {
            mLruCache.remove(url);
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
    public synchronized void putData(Data data) {
        if (DataUtils.isAvailable(data)) {
            mLruCache.put(data.url, data.bitmap);
        }
    }

    @Override
    public Bitmap cache(String url) {
        return mLruCache == null ? null : mLruCache.get(url);
    }

}
