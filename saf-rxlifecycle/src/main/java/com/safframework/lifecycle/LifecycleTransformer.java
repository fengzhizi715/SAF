package com.safframework.lifecycle;

import org.reactivestreams.Publisher;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.BehaviorProcessor;

/**
 * Created by Tony Shen on 2017/5/25.
 */

public class LifecycleTransformer<T> implements ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        SingleTransformer<T, T>,
        MaybeTransformer<T, T>,
        CompletableTransformer {

    private final BehaviorProcessor<Integer> lifecycleBehavior;

    private LifecycleTransformer() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public LifecycleTransformer(@NonNull BehaviorProcessor<Integer> lifecycleBehavior) {
        this.lifecycleBehavior = lifecycleBehavior;
    }

    @Override
    public CompletableSource apply(Completable upstream) {
        return upstream.ambWith(
                lifecycleBehavior.filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@LifecyclePublisher.Event Integer event) throws Exception {
                        return event == LifecyclePublisher.ON_DESTROY_VIEW ||
                                event == LifecyclePublisher.ON_DESTROY ||
                                event == LifecyclePublisher.ON_DETACH;
                    }
                }).take(1).flatMapCompletable(new Function<Integer, Completable>() {
                    @Override
                    public Completable apply(Integer flowable) throws Exception {
                        return Completable.complete();
                    }
                })
        );
    }

    @Override
    public Publisher<T> apply(final Flowable<T> upstream) {
        return upstream.takeUntil(
                lifecycleBehavior.skipWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(@LifecyclePublisher.Event Integer event) throws Exception {
                        return event != LifecyclePublisher.ON_DESTROY_VIEW &&
                                event != LifecyclePublisher.ON_DESTROY &&
                                event != LifecyclePublisher.ON_DETACH;
                    }
                })
        );
    }

    @Override
    public MaybeSource<T> apply(Maybe<T> upstream) {
        return upstream.takeUntil(
                lifecycleBehavior.skipWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(@LifecyclePublisher.Event Integer event) throws Exception {
                        return event != LifecyclePublisher.ON_DESTROY_VIEW &&
                                event != LifecyclePublisher.ON_DESTROY &&
                                event != LifecyclePublisher.ON_DETACH;
                    }
                })
        );
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.takeUntil(
                lifecycleBehavior.skipWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(@LifecyclePublisher.Event Integer event) throws Exception {
                        return event != LifecyclePublisher.ON_DESTROY_VIEW &&
                                event != LifecyclePublisher.ON_DESTROY &&
                                event != LifecyclePublisher.ON_DETACH;
                    }
                }).toObservable()
        );
    }

    @Override
    public SingleSource<T> apply(Single<T> upstream) {
        return upstream.takeUntil(
                lifecycleBehavior.skipWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(@LifecyclePublisher.Event Integer event) throws Exception {
                        return event != LifecyclePublisher.ON_DESTROY_VIEW &&
                                event != LifecyclePublisher.ON_DESTROY &&
                                event != LifecyclePublisher.ON_DETACH;
                    }
                })
        );
    }
}
