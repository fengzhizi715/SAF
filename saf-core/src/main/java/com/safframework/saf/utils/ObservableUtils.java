package com.safframework.saf.utils;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Tony Shen on 2016/11/29.
 */

public class ObservableUtils {

    public static Observable wrap(final Object obj) {

        if (obj==null)
            return null;

        return Observable.just(obj);
    }

    public static Observable wrap(Callable callable) {

        if (callable==null)
            return null;

        return Observable.fromCallable(callable);
    }

    /**
     * 跟compose()配合使用,比如ObservableUtils.wrap(obj).compose(toMain())
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> toMain() {

        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
