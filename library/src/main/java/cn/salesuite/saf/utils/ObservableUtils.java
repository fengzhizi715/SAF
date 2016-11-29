package cn.salesuite.saf.utils;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by Tony Shen on 2016/11/29.
 */

public class ObservableUtils {

    public static Observable wrap(final Object obj) {

        if (obj==null)
            return null;

        return Observable.defer(new Func0() {
            @Override
            public Observable call() {
                return Observable.just(obj);
            }
      });
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
    public static <T> Observable.Transformer<T, T> toMain() {
        return new Observable.Transformer<T, T>() {
           @Override
            public Observable<T> call(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
