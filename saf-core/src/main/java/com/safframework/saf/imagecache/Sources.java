package com.safframework.saf.imagecache;

import android.content.Context;
import android.widget.ImageView;

import com.safframework.saf.utils.SAFUtils;
import com.safframwork.tony.common.utils.Preconditions;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class Sources {

    private static final String TAG = "Sources";

    Context mContext;
    MemoryCacheObservable memoryCacheObservable;
    DiskCacheObservable diskCacheObservable;
    NetCacheObservable netCacheObservable;

    public Sources(Context mContext) {
        this.mContext = mContext;
        memoryCacheObservable = new MemoryCacheObservable();
        if (SAFUtils.hasSdcard()) {
            diskCacheObservable = new DiskCacheObservable(mContext);
        }
        netCacheObservable = new NetCacheObservable(memoryCacheObservable,mContext);
    }

    public ConnectableObservable<Data> getConnectableObservable(String url, ImageView imageView) {

        memoryCacheObservable.create(url);
        if (diskCacheObservable!=null)
            diskCacheObservable.create(mContext, url, 0);
        netCacheObservable.create(url,imageView);

        if (diskCacheObservable!=null) {
            return addCaches(memoryCacheObservable,diskCacheObservable,netCacheObservable);
        }
        return addCaches(memoryCacheObservable,netCacheObservable);
    }

    private ConnectableObservable<Data> addCaches(final CacheObservable... observables) {

        if (Preconditions.isNotBlank(observables)) {
            ArrayList<Observable<Data>> list = new ArrayList<>();

            for(CacheObservable t: observables)
                list.add(t.observable);

            ConnectableObservable<Data> connectableObservable = Observable.concat(Observable.from(list)).first(new Func1<Data, Boolean>() {
                @Override
                public Boolean call(Data data) {
                    return DataUtils.isAvailable(data);
                }
            }).publish();

            connectableObservable.subscribe(new Subscriber<Data>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Data data) {
                    if (DataUtils.isAvailable(data)) {
                        for(CacheObservable t: observables) {
                            t.putData(data);
                        }
                    }
                }
            });
            return connectableObservable;
        }

        return null;
    }
}
