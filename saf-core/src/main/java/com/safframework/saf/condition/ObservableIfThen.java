package com.safframework.saf.condition;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.internal.disposables.EmptyDisposable;

/**
 * Created by Tony Shen on 2017/5/9.
 */

final class ObservableIfThen<T> extends Observable<T> {

    final BooleanSupplier condition;

    final ObservableSource<? extends T> then;

    final ObservableSource<? extends T> orElse;

    ObservableIfThen(BooleanSupplier condition, ObservableSource<? extends T> then,
                     ObservableSource<? extends T> orElse) {
        this.condition = condition;
        this.then = then;
        this.orElse = orElse;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        boolean b;

        try {
            b = condition.getAsBoolean();
        } catch (Throwable ex) {
            EmptyDisposable.error(ex, observer);
            return;
        }

        if (b) {
            then.subscribe(observer);
        } else {
            orElse.subscribe(observer);
        }
    }
}
