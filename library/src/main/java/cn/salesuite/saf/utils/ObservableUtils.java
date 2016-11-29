package cn.salesuite.saf.utils;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.functions.Func0;

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
}
