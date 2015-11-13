package cn.salesuite.saf.rxjava.imagecache;

import rx.Observable;

/**
 * Created by Tony Shen on 15/11/13.
 */
public abstract class CacheObservable {

    public abstract Observable<Data> getObservable(String url);
}
