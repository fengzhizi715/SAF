package com.safframework.saf.condition;

import org.reactivestreams.Publisher;

import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by Tony Shen on 2017/5/9.
 */

public final class Statement {

    public static <R> Observable<R> ifThen(BooleanSupplier condition, Observable<? extends R> then) {
        return ifThen(condition, then, Observable.<R> empty());
    }

    public static <R> Observable<R> ifThen(BooleanSupplier condition, Observable<? extends R> then,
                                           Observable<? extends R> orElse) {
        return RxJavaPlugins.onAssembly(new ObservableIfThen<R>(condition, then, orElse));
    }

    public static <R> Flowable<R> ifThen(BooleanSupplier condition, Publisher<? extends R> then) {

        return ifThen(condition, then, Flowable.<R>empty());
    }

    public static <R> Flowable<R> ifThen(BooleanSupplier condition, Publisher<? extends R> then,
                                         Flowable<? extends R> orElse) {

        return RxJavaPlugins.onAssembly(new FlowableIfThen<R>(condition, then, orElse));
    }

    public static <K, R> Observable<R> switchCase(Callable<? extends K> caseSelector,
                                                  Map<? super K, ? extends Observable<? extends R>> mapOfCases,
                                                  Observable<? extends R> defaultCase) {
        return RxJavaPlugins.onAssembly(new ObservableSwitchCase<R, K>(caseSelector, mapOfCases, defaultCase));
    }

    public static <K, R> Flowable<R> switchCase(Callable<? extends K> caseSelector,
                                                Map<? super K, ? extends Publisher<? extends R>> mapOfCases,
                                                Publisher<? extends R> defaultCase) {

        return RxJavaPlugins.onAssembly(new FlowableSwitchCase<R, K>(caseSelector, mapOfCases, defaultCase));
    }

}
