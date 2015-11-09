package cn.salesuite.saf.rxjava;

import rx.Observable;

/**
 * Created by Tony Shen on 15/11/5.
 */
public class ObservableWrapper<T> {

    public String key;

    public Observable<T> observable;

    public ObservableWrapper(String key, Observable<T> observable) {
        this.key = key;
        this.observable = observable;
    }
}
