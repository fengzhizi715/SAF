package com.safframework.lifecycle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static RxLifecycle bind(@NonNull Fragment targetFragment) {
        return bind(targetFragment.getChildFragmentManager());
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
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

    public static RxLifecycle bind(@NonNull android.support.v4.app.Fragment targetFragment) {
        return bind(targetFragment.getChildFragmentManager());
    }

    public static RxLifecycle bind(@NonNull android.support.v4.app.FragmentManager fragmentManager) {
        BindingV4Fragment fragment = (BindingV4Fragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new BindingV4Fragment();

            final android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(fragment, FRAGMENT_TAG);
            transaction.commit();

        } else if (Build.VERSION.SDK_INT >= 13 && fragment.isDetached()) {
            final android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
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
