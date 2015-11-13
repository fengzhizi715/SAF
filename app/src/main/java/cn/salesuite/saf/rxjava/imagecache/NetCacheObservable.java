package cn.salesuite.saf.rxjava.imagecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import cn.salesuite.saf.imagecache.BitmapProcessor;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class NetCacheObservable extends CacheObservable {

    Context mContext;
    ImageView imageView;

    public NetCacheObservable(Context mContext) {
        this.mContext = mContext;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public Observable<Data> getObservable(final String url) {
        return Observable.create(new Observable.OnSubscribe<Data>() {
            @Override
            public void call(Subscriber<? super Data> subscriber) {
                Data data;
                Bitmap bitmap = null;

                BitmapProcessor processor = new BitmapProcessor(mContext);

                JobOptions options = new JobOptions(imageView);
                bitmap = processor.decodeSampledBitmapFromUrl(url, options.requestedWidth,
                        options.requestedHeight);

                data = new Data(bitmap, url);

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
