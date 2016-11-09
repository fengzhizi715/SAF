package cn.salesuite.saf.rxjava;

import android.app.Dialog;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tony Shen on 2016/11/9.
 */

public abstract class RxAsyncTask<T> {

    private Dialog mDialog;
    private SuccessHandler successHandler;
    private FailedHandler failedHandler;

    public RxAsyncTask() {
        this(null);
    }

    public RxAsyncTask(Dialog dialog) {
        this.mDialog = dialog;
        execute();
    }

    private void onPreExecute() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    private void execute() {
        onPreExecute();
        createObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        // no-op
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                            mDialog = null;
                        }

                        if (e != null && failedHandler!=null) {
                            failedHandler.onFail(e);
                        }
                    }

                    @Override
                    public void onNext(T t) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                            mDialog = null;
                        }

                        if (successHandler!=null) {
                            successHandler.onSuccess(t);
                        }
                    }
                });
    }

    private Observable<T> createObservable(){
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {

                subscriber.onNext(onExecute());
                subscriber.onCompleted();
            }
        });
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
     * 执行任务的结果
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
