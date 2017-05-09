package com.safframework.saf.condition;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.internal.subscriptions.EmptySubscription;

/**
 * Created by Tony Shen on 2017/5/9.
 */

final class FlowableSwitchCase<R, K> extends Flowable<R> {

    final Callable<? extends K> caseSelector;

    final Map<? super K, ? extends Publisher<? extends R>> mapOfCases;

    final Publisher<? extends R> defaultCase;

    FlowableSwitchCase(Callable<? extends K> caseSelector,
                       Map<? super K, ? extends Publisher<? extends R>> mapOfCases,
                       Publisher<? extends R> defaultCase) {
        this.caseSelector = caseSelector;
        this.mapOfCases = mapOfCases;
        this.defaultCase = defaultCase;
    }

    @Override
    protected void subscribeActual(Subscriber<? super R> s) {
        K key;
        Publisher<? extends R> source;

        try {
            key = caseSelector.call();
            source = mapOfCases.get(key);
        } catch (Throwable ex) {
            EmptySubscription.error(ex, s);
            return;
        }

        if (source == null) {
            source = defaultCase;
        }

        source.subscribe(s);
    }
}
