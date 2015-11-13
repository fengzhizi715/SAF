package cn.salesuite.saf.rxjava.imagecache;

import android.content.Context;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class Sources {

    private static final String TAG = "Sources";

    Context mContext;
    MemoryCacheOvservable mMemoryCacheOvservable;
    DiskCacheObservable mDiskCacheObservable;
    NetCacheObservable mNetCacheObservable;

    public Sources(Context mContext) {
        this.mContext = mContext;
        mMemoryCacheOvservable = new MemoryCacheOvservable();
        mDiskCacheObservable = new DiskCacheObservable(mContext);
        mNetCacheObservable = new NetCacheObservable(mContext);
    }

    public Sources(Context mContext,String fileDir) {
        this.mContext = mContext;
        mMemoryCacheOvservable = new MemoryCacheOvservable();
        mDiskCacheObservable = new DiskCacheObservable(mContext,fileDir);
        mNetCacheObservable = new NetCacheObservable(mContext);
    }

    public Observable<Data> memory(String url) {
        return mMemoryCacheOvservable.getObservable(url);
    }

    public Observable<Data> disk(String url) {
        return mDiskCacheObservable.getObservable(url)
                .filter(new Func1<Data, Boolean>() {
                    public Boolean call(Data data) {
                        if (data.bitmap != null) {
                            return true;
                        }
                        return false;
                    }
                })
                .doOnNext(new Action1<Data>() {
                    public void call(Data data) {
                        //save picture to disk
                        mMemoryCacheOvservable.putData(data);
                    }
                });
    }

    public Observable<Data> network(String url) {
        return mNetCacheObservable.getObservable(url)
                .doOnNext(new Action1<Data>() {
                    public void call(Data data) {
                        //save picture to disk and memory
                        mMemoryCacheOvservable.putData(data);
                        mDiskCacheObservable.putData(data);
                    }
                });
    }
}
