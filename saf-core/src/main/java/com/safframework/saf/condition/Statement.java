package com.safframework.saf.condition;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.BooleanSupplier;

/**
 * Created by Tony Shen on 2017/5/9.
 */

public final class Statement {

    public static <R> Observable<R> ifThen(BooleanSupplier condition, Observable<? extends R> then) {
        return Observable.create(new OperatorIfThen<R>(condition, then, Observable.<R> empty()));
    }

    public static <R> Observable<R> ifThen(BooleanSupplier condition, Observable<? extends R> then,
                                           Observable<? extends R> orElse) {
        return Observable.create(new OperatorIfThen<R>(condition, then, orElse));
    }

    public static <K, R> Observable<R> switchCase(Func0<? extends K> caseSelector,
                                                  Map<? super K, ? extends Observable<? extends R>> mapOfCases,
                                                  Observable<? extends R> defaultCase) {
        return Observable.create(new OperatorSwitchCase<K, R>(caseSelector, mapOfCases, defaultCase));
    }
}
