package com.safframework.saf.async;

import android.app.Dialog;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Tony Shen on 2016/11/9.
 */

public abstract class RxAsyncTask<T> {

    private Dialog mDialog;
    private SuccessHandler successHandler;
    private FailedHandler failedHandler;
    private int retryCount = 3;

    private CompositeDisposable composite = new CompositeDisposable();

    public RxAsyncTask() {
        this(null);
    }

    public RxAsyncTask(Dialog dialog) {
        this.mDialog = dialog;
    }

    private void onPreExecute() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    private void execute() {
        onPreExecute();
        Flowable<T> flowable = createFlowable();

        if (retryCount > 0) {
            composite.add(flowable.retryWhen(new RetryWithDelay(retryCount, 1000))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<T>() {

                        @Override
                        public void accept(@NonNull T t) throws Exception {

                            if (mDialog != null) {
                                mDialog.dismiss();
                                mDialog = null;
                            }

                            if (successHandler != null) {
                                successHandler.onSuccess(t);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            if (mDialog != null) {
                                mDialog.dismiss();
                                mDialog = null;
                            }

                            if (throwable != null && failedHandler != null) {
                                failedHandler.onFail(throwable);
                            }
                        }
                    }));
        } else {
            composite.add(flowable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<T>() {
                        @Override
                        public void accept(@NonNull T t) throws Exception {
                            if (mDialog != null) {
                                mDialog.dismiss();
                                mDialog = null;
                            }

                            if (successHandler != null) {
                                successHandler.onSuccess(t);
                            }
                        }

                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            if (mDialog != null) {
                                mDialog.dismiss();
                                mDialog = null;
                            }

                            if (throwable != null && failedHandler != null) {
                                failedHandler.onFail(throwable);
                            }
                        }
                    }));
        }

    }

    /**
     * 所有的task都必须执行这个方法，否则无法运行
     */
    public void start() {
        execute();
    }

    /**
     * 取消task的执行
     */
    public void cancel() {
        composite.clear();
    }

    private Flowable<T> createFlowable() {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<T> e) throws Exception {
                e.onNext(onExecute());
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER);
    }

    public RxAsyncTask success(SuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public RxAsyncTask failed(FailedHandler failedHandler) {
        this.failedHandler = failedHandler;
        return this;
    }

    /**
     * 重试次数，默认是3次，可以根据实际情况修改
     * @param retryCount
     * @return
     */
    public RxAsyncTask retry(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    /**
     * 可以在Activity/Fragment注销时使用，防止内存泄露
     */
    public void destory() {

        if (!composite.isDisposed()) {
            composite.dispose();
        }
    }

    /**
     * 执行任务的结果
     *
     * @return
     */
    public abstract T onExecute();

    public interface SuccessHandler<T> {

        void onSuccess(T t);
    }

    public interface FailedHandler {

        void onFail(Throwable e);
    }
}
