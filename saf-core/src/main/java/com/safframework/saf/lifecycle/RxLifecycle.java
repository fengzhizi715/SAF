package com.safframework.saf.lifecycle;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by Tony Shen on 2017/5/25.
 */

public class RxLifecycle {

    private static final String FRAGMENT_TAG = "_BINDING_FRAGMENT_";
    private final LifecyclePublisher lifecyclePublisher;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static RxLifecycle bind(@NonNull AppCompatActivity targetActivity) {
        return bind(targetActivity.getSupportFragmentManager());
    }

    public static RxLifecycle bind(@NonNull Fragment targetFragment) {
        return bind(targetFragment.getChildFragmentManager());
    }

    public static RxLifecycle bind(@NonNull FragmentManager fragmentManager) {
        BindingFragment fragment = (BindingFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new BindingFragment();

            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(fragment, FRAGMENT_TAG);
            transaction.commit();

        } else if (Build.VERSION.SDK_INT >= 13 && fragment.isDetached()) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.attach(fragment);
            transaction.commit();
        }

        return bind(fragment.getLifecyclePublisher());
    }

    public static RxLifecycle bind(@NonNull LifecyclePublisher lifecyclePublisher) {
        return new RxLifecycle(lifecyclePublisher);
    }

    private RxLifecycle() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    private RxLifecycle(@NonNull LifecyclePublisher lifecyclePublisher) {
        this.lifecyclePublisher = lifecyclePublisher;
    }

    public Flowable<Integer> asFlowable() {
        return lifecyclePublisher.getBehavior();
    }

    public Observable<Integer> asObservable() {
        return lifecyclePublisher.getBehavior().toObservable();
    }

    public <T> LifecycleTransformer<T> toLifecycleTransformer() {
        return new LifecycleTransformer<T>(lifecyclePublisher.getBehavior());
    }
}
