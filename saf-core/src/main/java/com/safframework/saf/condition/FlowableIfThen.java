package com.safframework.saf.condition;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.reactivex.Flowable;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.internal.subscriptions.EmptySubscription;

/**
 * Created by Tony Shen on 2017/5/9.
 */

final class FlowableIfThen<T> extends Flowable<T> {

    final BooleanSupplier condition;

    final Publisher<? extends T> then;

    final Publisher<? extends T> orElse;

    FlowableIfThen(BooleanSupplier condition, Publisher<? extends T> then,
                   Publisher<? extends T> orElse) {
        this.condition = condition;
        this.then = then;
        this.orElse = orElse;
    }

    @Override
    protected void subscribeActual(Subscriber<? super T> s) {
        boolean b;

        try {
            b = condition.getAsBoolean();
        } catch (Throwable ex) {
            EmptySubscription.error(ex, s);
            return;
        }

        if (b) {
            then.subscribe(s);
        } else {
            orElse.subscribe(s);
        }
    }
}
