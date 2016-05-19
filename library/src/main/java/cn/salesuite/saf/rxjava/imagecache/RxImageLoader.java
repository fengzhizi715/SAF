package cn.salesuite.saf.rxjava.imagecache;

import android.content.Context;
import android.widget.ImageView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.salesuite.saf.utils.SAFUtils;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class RxImageLoader {

    private Sources sources;
    private static String fileDir;
    private boolean enableDiskCache = true; // 默认打开sd卡的缓存

    public void init(Context mContext) {
        sources = new Sources(mContext);
        if (SAFUtils.isMOrHigher() && !SAFUtils.verifyStoragePermissions(mContext)) {
            enableDiskCache = false;
        }
    }

    public void init(Context mContext,String mFileDir) {
        sources = new Sources(mContext,mFileDir);
        fileDir = mFileDir;
        if (SAFUtils.isMOrHigher() && !SAFUtils.verifyStoragePermissions(mContext)) {
            enableDiskCache = false;
        }
    }

    private static final Map<Integer, String> cacheKeysMap = Collections.synchronizedMap(new HashMap<Integer, String>());

    /**
     *
     * @param imageView
     * @param url
     * @return
     */
    public void displayImage(final ImageView imageView, final String url) {
        if (imageView != null) {
            cacheKeysMap.put(imageView.hashCode(), url);
        }

        sources.mNetCacheObservable.setImageView(imageView);

        Observable<Data> source = null;

        if (enableDiskCache) {
            source = Observable.concat(sources.memory(url), sources.disk(url),
                    sources.network(url))
                    .first(new Func1<Data, Boolean>() {
                        public Boolean call(Data data) {
                            return data != null && data.isAvailable() && url.equals(data.url);
                        }
                    });
        } else {
            source = Observable.concat(sources.memory(url), sources.network(url))
                    .first(new Func1<Data, Boolean>() {
                        public Boolean call(Data data) {
                            return data != null && data.isAvailable() && url.equals(data.url);
                        }
                    });
        }

        source.subscribe(new Action1<Data>(){
            @Override
            public void call(Data data) {
                if (imageView != null && url.equals(cacheKeysMap.get(imageView.hashCode()))) {
                    imageView.setImageBitmap(data.bitmap);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                clearMemCache();
            }
        });
    }

    /**
     *
     * @param imageView
     * @param url
     * @param default_img_id 本地存放默认图片的资源id
     * @return
     */
    public void displayImage(final ImageView imageView, final String url,int default_img_id) {
        if (imageView != null) {
            cacheKeysMap.put(imageView.hashCode(), url);
        }

        imageView.setImageResource(default_img_id);

        sources.mNetCacheObservable.setImageView(imageView);

        Observable<Data> source = null;

        if (enableDiskCache) {
            source = Observable.concat(sources.memory(url), sources.disk(url),
                    sources.network(url))
                    .first(new Func1<Data, Boolean>() {
                        public Boolean call(Data data) {
                            return data != null && data.isAvailable() && url.equals(data.url);
                        }
                    });
        } else {
            source = Observable.concat(sources.memory(url), sources.network(url))
                    .first(new Func1<Data, Boolean>() {
                        public Boolean call(Data data) {
                            return data != null && data.isAvailable() && url.equals(data.url);
                        }
                    });
        }

        source.subscribe(new Action1<Data>(){
            @Override
            public void call(Data data) {
                if (imageView != null && url.equals(cacheKeysMap.get(imageView.hashCode()))) {
                    imageView.setImageBitmap(data.bitmap);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                clearMemCache();
            }
        });
    }

    public boolean isEnableDiskCache() {
        return enableDiskCache;
    }

    public void setEnableDiskCache(boolean enableDiskCache) {
        this.enableDiskCache = enableDiskCache;
    }

    /**
     * 清空内存中的缓存
     */
    public void clearMemCache() {
        sources.mMemoryCacheOvservable.clear();
    }

    /**
     * 清空所有的缓存
     */
    public void clearAllCache() {
        sources.mMemoryCacheOvservable.clear();
        if (enableDiskCache) {
            sources.mDiskCacheObservable.clear();
        }
    }
}
