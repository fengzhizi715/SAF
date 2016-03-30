package cn.salesuite.saf.rxjava;

import android.app.Dialog;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tony Shen on 16/3/28.
 */
public abstract class RxAsyncTask {

    private Dialog mDialog;

    public RxAsyncTask() {
    }

    public RxAsyncTask(Dialog dialog) {
        this.mDialog = dialog;
    }

    protected void onPreExecute() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public void execute(final HttpResponseHandler callback) {
        onPreExecute();
        createObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
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

                        if (e != null) {
                            callback.onFail(e);
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        if (mDialog != null) {
                            mDialog.dismiss();
                            mDialog = null;
                        }
                        callback.onSuccess(s);
                    }
                });
    }

    private Observable<String> createObservable(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext(onExecute());
                subscriber.onCompleted();
            }
        });
    }

    /**
     * 返回网络请求的结果
     * @return
     */
    public abstract String onExecute();

    public interface HttpResponseHandler {

        /**
         * http请求成功后，response转换成content
         * @param content
         */
        void onSuccess(String content);

        /**
         * http请求失败后，response转换成jsonString
         *
         * @param e
         */
        void onFail(Throwable e);
    }
}
