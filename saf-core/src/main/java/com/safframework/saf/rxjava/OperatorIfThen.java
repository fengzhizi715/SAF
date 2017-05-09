package com.safframework.saf.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;

/**
 * Created by Tony Shen on 2017/5/9.
 */

public class OperatorIfThen<R> implements ObservableOnSubscribe<R> {

    final BooleanSupplier condition;
    final Observable<? extends R> then;
    final Observable<? extends R> orElse;

    public OperatorIfThen(BooleanSupplier condition, Observable<? extends R> then, Observable<? extends R> orElse) {
        this.condition = condition;
        this.then = then;
        this.orElse = orElse;
    }

    @Override
    public void subscribe(final ObservableEmitter<R> e) throws Exception {
        Observable<? extends R> target;
        try {
            if (condition.getAsBoolean()) {
                target = then;
            } else {
                target = orElse;
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
