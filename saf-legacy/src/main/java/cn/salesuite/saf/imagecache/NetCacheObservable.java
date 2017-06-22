package cn.salesuite.saf.imagecache;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;

import cn.salesuite.saf.utils.IOUtils;
import cn.salesuite.saf.utils.SAFUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class NetCacheObservable extends CacheObservable {

    private MemoryCacheObservable memoryCacheObservable;
    private Context mContext;
    private static final int DEFAULT_DENSITY = 240;
    private static final float SCALE_FACTOR = 0.75f;

    public NetCacheObservable(MemoryCacheObservable memoryCacheObservable, Context context) {
        this.memoryCacheObservable = memoryCacheObservable;
        this.mContext = context;
    }

    public void create(final String url, final ImageView imageView) {

        this.observable = Observable.create(new ObservableOnSubscribe<Data>() {
            @Override
            public void subscribe(ObservableEmitter<Data> e) throws Exception {
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

                    options.inSampleSize = calculateInSampleSize(options,imageView.getWidth(),imageView.getHeight());
                    options.inJustDecodeBounds = false;
                    if (Build.VERSION.SDK_INT <= 10) {
                        Field field = null;
                        try {
                            field = BitmapFactory.Options.class.getDeclaredField("inNativeAlloc");
                            field.setAccessible(true);
                            field.setBoolean(options, true);
                        } catch (NoSuchFieldException exception) {
                            exception.printStackTrace();
                        } catch (IllegalAccessException exception) {
                            exception.printStackTrace();
                        }
                    }

                    if (mContext!=null) {
                        int displayDensityDpi = mContext.getResources().getDisplayMetrics().densityDpi;
                        float displayDensity = mContext.getResources().getDisplayMetrics().density;
                        if (displayDensityDpi > DEFAULT_DENSITY && displayDensity > 1.5f) {
                            int density = (int) (displayDensityDpi * SCALE_FACTOR);
                            options.inDensity = density;
                            options.inTargetDensity = density;
                        }
                    }

                    if (SAFUtils.isHoneycombOrHigher()) {
                        addInBitmapOptions(options);
                    }
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                } catch (IOException exception) {
                    exception.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
                data = new Data(bitmap, url);
                e.onNext(data);
                e.onComplete();
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

        if (memoryCacheObservable!=null) {
            Bitmap inBitmap = memoryCacheObservable.getBitmapFromReusableSet(options);
            if (inBitmap != null && !inBitmap.isRecycled()) {
                options.inBitmap = inBitmap;
                options.inMutable = true;
            }
        }

        return options;
    }
}
