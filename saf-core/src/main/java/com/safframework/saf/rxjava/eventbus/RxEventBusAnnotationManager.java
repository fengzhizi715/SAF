package com.safframework.saf.rxjava.eventbus;

import android.util.Log;

import com.safframework.saf.concurrent.BackgroundExecutor;
import com.safframwork.tony.common.reflect.Reflect;
import com.safframwork.tony.common.utils.Preconditions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Tony Shen on 15/11/5.
 */
public class RxEventBusAnnotationManager {

    private static final String TAG = RxEventBusAnnotationManager.class.getName();
    private Object object;
    private List<ObservableWrapper> registeredObservable;

    public RxEventBusAnnotationManager(Object object) {
        this.object = object;

        init();
    }

    private void init() {
        Method[] methods = object.getClass().getDeclaredMethods();

        if (Preconditions.isNotBlank(methods)) {
            for (Method method:methods) {
                if (method!=null && method.isAnnotationPresent(Subscribe.class)) {
                    try {
                        parserObservableEventAnnotations(method);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private<T> void parserObservableEventAnnotations(Method method) throws Exception{
        Class[] params = method.getParameterTypes();

        if (params == null || params.length!=1) {
            throw new Exception("the method[" + method.getName() + "] must defined xxx(T object)");
        }

        Subscribe subscribe = method.getAnnotation(Subscribe.class);
        ThreadMode threadMode = subscribe.value();

        // 默认clazz参数类型
        Class<T> targetClazz = params[0];

        registerObservable(method, targetClazz, threadMode);
    }

    private<T> void registerObservable(final Method method, final Class<T> clazz, ThreadMode threadMode) {
        if (clazz == null) {
            return;
        }

        if (registeredObservable == null) {
            registeredObservable = new ArrayList<ObservableWrapper>();
        }

        Observable<T> observable = RxEventBus.getInstance().register(clazz);

        registeredObservable.add(new ObservableWrapper(clazz.getName(), observable));

        Observable<T> schedulerObservable = null;
        switch (threadMode) {
            case PostThread: // MAIN_THREAD
                schedulerObservable = observable.observeOn(AndroidSchedulers.mainThread());
                break;

            case BackgroundThread:
                schedulerObservable = observable.subscribeOn(Schedulers.from(new BackgroundExecutor())).observeOn(AndroidSchedulers.mainThread());
                break;

            case IO:
                schedulerObservable = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
                break;

            default: // MAIN_THREAD default
                schedulerObservable = observable.observeOn(AndroidSchedulers.mainThread());
                break;
        }

        if (schedulerObservable == null) {
            return;
        }

        schedulerObservable.subscribe(new Action1<T>() {
            @Override
            public void call(T t) {
                try {
                    Reflect.on(object).call(method.getName(), t);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    public void clear() {
        if (Preconditions.isNotBlank(registeredObservable)) {
            for (ObservableWrapper observableWrapper : registeredObservable) {
                RxEventBus.getInstance().unregister(observableWrapper.key, observableWrapper.observable);
            }
        }
    }

}