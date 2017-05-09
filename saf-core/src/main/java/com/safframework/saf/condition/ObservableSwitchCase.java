package com.safframework.saf.condition;

import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.internal.disposables.EmptyDisposable;

/**
 * Created by Tony Shen on 2017/5/10.
 */

final class ObservableSwitchCase<T, K> extends Observable<T> {

    final Callable<? extends K> caseSelector;

    final Map<? super K, ? extends ObservableSource<? extends T>> mapOfCases;

    final ObservableSource<? extends T> defaultCase;

    ObservableSwitchCase(Callable<? extends K> caseSelector,
                         Map<? super K, ? extends ObservableSource<? extends T>> mapOfCases,
                         ObservableSource<? extends T> defaultCase) {
        this.caseSelector = caseSelector;
        this.mapOfCases = mapOfCases;
        this.defaultCase = defaultCase;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        K key;
        ObservableSource<? extends T> source;

        try {
            key = caseSelector.call();

            source = mapOfCases.get(key);
        } catch (Throwable ex) {
            EmptyDisposable.error(ex, observer);
            return;
        }

        if (source == null) {
            source = defaultCase;
        }

        source.subscribe(observer);
    }
}
