/**
 * 
 */
package cn.salesuite.saf.imagecache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;
import cn.salesuite.saf.config.SAFConfig;

import com.jakewharton.disklrucache.DiskLruCache;

/**
 * @author Tony Shen
 *
 */
public class DiskLruImageCache {
	private static final String TAG = "DiskLruImageCache";
    private DiskLruCache mDiskCache;
    private static CompressFormat mCompressFormat = CompressFormat.JPEG;
    private static int mCompressQuality = 70;
    private static final int APP_VERSION = 1;
    private static final int VALUE_COUNT = 1;
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
    private static final int IO_BUFFER_SIZE = 8 * 1024;

    public DiskLruImageCache(Context context) {
        this(context, mCompressFormat, mCompressQuality);
    }
    
    public DiskLruImageCache(Context context,String fileDir) {
        this(context, mCompressFormat, mCompressQuality, fileDir);
    }

    public DiskLruImageCache(Context context, CompressFormat compressFormat, int quality) {
        try {
            final File diskCacheDir = getDiskCacheDir(context, SAFConfig.CACHE_DIR);
            mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, DiskLruImageCache.DISK_CACHE_SIZE);
            mCompressFormat = compressFormat;
            mCompressQuality = quality;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public DiskLruImageCache(Context context, CompressFormat compressFormat, int quality, String fileDir) {
        try {
            final File diskCacheDir = getDiskCacheDir(context, fileDir);
            mDiskCache = DiskLruCache.open(diskCacheDir, APP_VERSION, VALUE_COUNT, DiskLruImageCache.DISK_CACHE_SIZE);
            mCompressFormat = compressFormat;
            mCompressQuality = quality;
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private boolean writeBitmapToFile(final Bitmap bitmap, final DiskLruCache.Editor editor) throws IOException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(editor.newOutputStream(0), IO_BUFFER_SIZE);
            return bitmap.compress(mCompressFormat, mCompressQuality, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void put(final String key, final Bitmap data) {

        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskCache.edit(key);
            if (editor == null)
                return;

            if (writeBitmapToFile(data, editor)) {
                editor.commit();
            } else {
                editor.abort();
                Log.e(TAG, "ERROR on: image put on disk cache " + key);
            }
        } catch (final IOException e) {
            Log.e(TAG, "ERROR on: image put on disk cache " + key, e);
            try {
                if (editor != null) {
                    editor.abort();
                }
            } catch (final IOException ignored) {}
        }

    }

    public Bitmap getBitmap(final String key) {
        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskCache.get(key);

            if (snapshot == null) 
            	return null;
               
            final InputStream in = snapshot.getInputStream(0);
            if (in != null) {
                final BufferedInputStream buffIn = new BufferedInputStream(in, IO_BUFFER_SIZE);
                bitmap = BitmapProcessor.decodeStream(buffIn);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        return bitmap;
    }
    
    public boolean containsKey(final String key) {
        boolean contained = false;
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskCache.get(key);
            contained = (snapshot != null);
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }

        return contained;
    }

    public void clearCache() {
        try {
            mDiskCache.delete();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
	/**
	 * 根据key删除DiskLruImageCache的图片缓存
	 * @param url
	 */
	public void remove(String url) {
		if (containsKey(url)) {
			try {
				mDiskCache.remove(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
