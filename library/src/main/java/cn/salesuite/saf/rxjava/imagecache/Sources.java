package cn.salesuite.saf.rxjava.imagecache;

import android.content.Context;
import android.widget.ImageView;

import java.util.ArrayList;

import cn.salesuite.saf.utils.Preconditions;
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
        diskCacheObservable = new DiskCacheObservable(mContext);
        netCacheObservable = new NetCacheObservable();
    }

    public ConnectableObservable<Data> getConnectableObservable(String url, ImageView imageView) {

        memoryCacheObservable.create(url);
        diskCacheObservable.create(mContext, url, 0);
        netCacheObservable.create(url,imageView);

        return addCaches(memoryCacheObservable,diskCacheObservable,netCacheObservable);
    }

    private ConnectableObservable<Data> addCaches(final CacheObservable... observables) {

        if (Preconditions.isNotBlank(observables)) {
            ArrayList<Observable<Data>> list = new ArrayList<>();

            for(CacheObservable t: observables)
                list.add(t.observable);

            ConnectableObservable<Data> connectableObservable = Observable.concat(Observable.from(list)).first(new Func1<Data, Boolean>() {
                @Override
                public Boolean call(Data data) {
                    return (data != null && data.isAvailable());
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
                    for(CacheObservable t: observables) {
                        t.putData(data);
                    }
                }
            });
            return connectableObservable;
        }

        return null;
    }
}
