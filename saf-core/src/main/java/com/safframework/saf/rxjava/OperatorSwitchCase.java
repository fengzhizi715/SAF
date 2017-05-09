package com.safframework.saf.rxjava;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Tony Shen on 2017/5/9.
 */

public class OperatorSwitchCase<K, R> implements ObservableOnSubscribe<R> {

    final Func0<? extends K> caseSelector;
    final Map<? super K, ? extends Observable<? extends R>> mapOfCases;
    final Observable<? extends R> defaultCase;

    public OperatorSwitchCase(Func0<? extends K> caseSelector,
                              Map<? super K, ? extends Observable<? extends R>> mapOfCases,
                              Observable<? extends R> defaultCase) {
        this.caseSelector = caseSelector;
        this.mapOfCases = mapOfCases;
        this.defaultCase = defaultCase;
    }

    @Override
    public void subscribe(final ObservableEmitter<R> e) throws Exception {
        Observable<? extends R> target;
        try {
            K caseKey = caseSelector.call();
            if (mapOfCases.containsKey(caseKey)) {
                target = mapOfCases.get(caseKey);
            } else {
                target = defaultCase;
            }
        } catch (Throwable t) {
            e.onError(t);
            return;
        }

        target.subscribe(new Consumer<R>() {
            @Override
            public void accept(@NonNull R r) throws Exception {

                e.onNext(r);
            }
        });
    }
}
