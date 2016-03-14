package cn.salesuite.saf.rxjava.imagecache;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.salesuite.saf.config.SAFConstant;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class DiskCacheObservable extends CacheObservable {

    Context mContext;
    File mCacheFile;

    public DiskCacheObservable(Context mContext) {
        this(mContext, SAFConstant.CACHE_DIR);
    }

    public DiskCacheObservable(Context mContext,String fileDir) {
        this.mContext = mContext;
        mCacheFile = getDiskCacheDir(mContext,fileDir);
    }

    private File getDiskCacheDir(final Context context, String fileDir) {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),fileDir);
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists()) {
            boolean b = cacheDir.mkdirs();
            if (!b) {
                cacheDir = context.getCacheDir();
                if (!cacheDir.exists())
                    cacheDir.mkdirs();
            }
        }

        return cacheDir;
    }

    @Override
    public Observable<Data> getObservable(final String url) {
        return Observable.create(new Observable.OnSubscribe<Data>() {
            @Override
            public void call(Subscriber<? super Data> subscriber) {
                File f = getFile(url);
                Data data = new Data(f, url);
                subscriber.onNext(data);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private File getFile(String url) {
        url = url.replaceAll(File.separator, "-");
        return new File(mCacheFile, url);
    }

    /**
     * save pictures downloaded from net to disk
     * @param data data to be saved
     */
    public void putData(final Data data) {
        Observable.create(new Observable.OnSubscribe<Data>() {
            @Override
            public void call(Subscriber<? super Data> subscriber) {
                File f = getFile(data.url);
                OutputStream out = null;
                try {
                    out = new FileOutputStream(f);
                    Bitmap.CompressFormat format;
                    if (data.url.endsWith("png") || data.url.endsWith("PNG")) {
                        format = Bitmap.CompressFormat.PNG;
                    } else {
                        format = Bitmap.CompressFormat.JPEG;
                    }

                    if (data.bitmap!=null) {
                        data.bitmap.compress(format, 100, out);
                    }
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public void clear() {
        mCacheFile.delete();
    }
}
