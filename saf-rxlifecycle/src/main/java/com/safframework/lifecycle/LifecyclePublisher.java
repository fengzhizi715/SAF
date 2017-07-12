package com.safframework.lifecycle;

import android.support.annotation.IntDef;

import io.reactivex.processors.BehaviorProcessor;

/**
 * Created by Tony Shen on 2017/5/25.
 */

public class LifecyclePublisher {

    public static final int ON_ATTACH = 0;
    public static final int ON_CREATE = 1;
    public static final int ON_CREATE_VIEW = 2;
    public static final int ON_START = 3;
    public static final int ON_RESUME = 4;
    public static final int ON_PAUSE = 5;
    public static final int ON_STOP = 6;
    public static final int ON_DESTROY_VIEW = 7;
    public static final int ON_DESTROY = 8;
    public static final int ON_DETACH = 9;

    private final BehaviorProcessor<Integer> behavior = BehaviorProcessor.create();

    @IntDef({ON_ATTACH, ON_CREATE, ON_CREATE_VIEW,
            ON_START, ON_RESUME,
            ON_PAUSE, ON_STOP,
            ON_DESTROY_VIEW, ON_DESTROY, ON_DETACH})
    public @interface Event {
    }

    public BehaviorProcessor<Integer> getBehavior() {
        return behavior;
    }

    public void onAttach() {
        behavior.onNext(ON_ATTACH);
    }

    public void onCreate() {
        behavior.onNext(ON_CREATE);
    }

    public void onCreateView() {
        behavior.onNext(ON_CREATE_VIEW);
    }

    public void onStart() {
        behavior.onNext(ON_START);
    }

    public void onResume() {
        behavior.onNext(ON_RESUME);
    }

    public void onPause() {
        behavior.onNext(ON_PAUSE);
    }

    public void onStop() {
        behavior.onNext(ON_STOP);
    }

    public void onDestroyView() {
        behavior.onNext(ON_DESTROY_VIEW);
    }

    public void onDestroy() {
        behavior.onNext(ON_DESTROY);
    }

    public void onDetach() {
        behavior.onNext(ON_DETACH);
    }
}