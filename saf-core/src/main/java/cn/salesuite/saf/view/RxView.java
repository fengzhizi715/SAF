package cn.salesuite.saf.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.BehaviorSubject;

/**
 * 提供Observable化的View
 * Created by Tony Shen on 16/3/7.
 */
public class RxView {

    /**
     * 将view转换成Observable<View>
     * @param view
     * @return
     */
    public static Observable<View> clicks(final View view) {
        return Observable.create(new Observable.OnSubscribe<View>() {
            @Override
            public void call(final Subscriber<? super View> subscriber) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (subscriber.isUnsubscribed())
                            return;

                        subscriber.onNext(v);
                    }
                });
            }
        });
    }

    /**
     * 防止某个view重复点击
     * <pre>
     * <code>
     * RxView.preventMultipleClicks(button).subscribe(new Observer<Object>() {
     *      @Override
     *      public void onCompleted() {
     *         L.i("completed");
     *      }
     *
     *     @Override
     *      public void onError(Throwable e) {
     *        L.i("error");
     *      }
     *
     *      @Override
     *     public void onNext(Object object) {
     *        L.i("button clicked");
     *     }
     * });
     * </code>
     * </pre>
     * 或者
     * <pre>
     * <code>
     * RxView.preventMultipleClicks(button).subscribe(new Action1<View>() {
     *      @Override
     *      public void call(View view) {
     *
     *      }
     * });
     * </code>
     * </pre>
     * @param v
     * @return
     */
    public static Observable<View> preventMultipleClicks(View v) {
        return RxView.clicks(v).throttleFirst(1, TimeUnit.SECONDS);
    }

    public static Observable<String> text(EditText view) {
        String currentText = String.valueOf(view.getText());
        final BehaviorSubject<String> subject = BehaviorSubject.create(currentText);
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

            @Override
            public void afterTextChanged(Editable editable) {
                subject.onNext(editable.toString());
            }
        });
        return subject;
    }
}
