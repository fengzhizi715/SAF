package cn.salesuite.saf.rxjava.imagecache;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import cn.salesuite.saf.utils.IOUtils;
import cn.salesuite.saf.utils.SAFUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class NetCacheObservable extends CacheObservable {

    private MemoryCacheObservable memoryCacheObservable;

    public NetCacheObservable(MemoryCacheObservable memoryCacheObservable) {
        this.memoryCacheObservable = memoryCacheObservable;
    }

    public void create(final String url, final ImageView imageView) {

        this.observable = Observable.create(new Observable.OnSubscribe<Data>() {
            @Override
            public void call(Subscriber<? super Data> subscriber) {
                Data data;
                Bitmap bitmap = null;
                InputStream inputStream = null;
                try {
                    final URLConnection con = new URL(url).openConnection();
                    inputStream = new PatchInputStream(con.getInputStream());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    byte[] bytes = IOUtils.readInputStream(inputStream);
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                    options.inPurgeable = true;
                    options.inDither = false;
                    options.inInputShareable = true;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;

                    JobOptions jobOptions = new JobOptions(imageView);
                    options.inSampleSize = calculateInSampleSize(options,jobOptions.requestedWidth,jobOptions.requestedHeight);
                    options.inJustDecodeBounds = false;

                    if (SAFUtils.isHoneycombOrHigher()) {
                        addInBitmapOptions(options);
                    }
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
                data = new Data(bitmap, url);
                if(!subscriber.isUnsubscribed()) {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void putData(Data data) {

    }

    @Override
    public Bitmap cache(String info) {
        return null;
    }

    // Took from:
    // http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 设置inBitmap属性
     *
     * @param options options
     */
    @TargetApi(11)
    private BitmapFactory.Options addInBitmapOptions(BitmapFactory.Options options) {
        options.inMutable = true;

        if (memoryCacheObservable!=null) {
            Bitmap inBitmap = memoryCacheObservable.getBitmapFromReusableSet(options);
            if (inBitmap != null) {
                options.inBitmap = inBitmap;
            }
        }

        return options;
    }

    public static class JobOptions {

        public int requestedWidth;
        public int requestedHeight;

        public JobOptions() {
            this(0, 0);
        }

        public JobOptions(final ImageView imgView) {
            this(imgView.getWidth(), imgView.getHeight());
        }

        public JobOptions(final int requestedWidth, final int requestedHeight) {
            this.requestedWidth = requestedWidth;
            this.requestedHeight = requestedHeight;
        }
    }
}
