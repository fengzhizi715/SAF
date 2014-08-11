/**
 * 
 */
package cn.salesuite.saf.imagecache;

import java.io.InputStream;
import java.net.ResponseCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * @author Tony Shen
 *
 */
public class BitmapProcessor {

    public BitmapProcessor(final Context context) {
        ResponseCache.setDefault(new ImageResponseCache(context.getCacheDir()));
    }
    
	// Took from:
	// http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
	private static int calculateInSampleSize(
			final BitmapFactory.Options options, final int reqWidth,
			final int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if ((height > reqHeight) || (width > reqWidth)) {

			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = Math.min(heightRatio, widthRatio);
		}

		return inSampleSize;
	}
	
	public static Bitmap decodeStream(final InputStream stream) {
		try {
			return BitmapFactory.decodeStream(stream);
		} catch (final OutOfMemoryError e) {
			Log.e("BitmapProcessor", "Out of memory in decodeStream()");
			return null;
		}
	}

	public static Bitmap decodeStream(final InputStream stream,
			final BitmapFactory.Options options) {
		try {
			return BitmapFactory.decodeStream(stream, null, options);
		} catch (final OutOfMemoryError e) {
			Log.e("BitmapProcessor", "Out of memory in decodeStream()");
			return null;
		}
	}

	/**
	 * Decodes a sampled Bitmap from the provided url in the requested width and
	 * height
	 * 
	 * @param urlString
	 *            URL to download the bitmap from
	 * @param reqWidth
	 *            Requested width
	 * @param reqHeight
	 *            Requested height
	 * @return Decoded bitmap
	 */
	public Bitmap decodeSampledBitmapFromUrl(final String urlString,
			int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		options.inDither = false;
		options.inInputShareable = true;

		final BitmapConnection bitmapConnection = new BitmapConnection();
		bitmapConnection.readStream(urlString,
				new BitmapConnection.Runnable<Void>() {
					@Override
					public Void run(final InputStream stream) {
						decodeStream(stream, options);
						return null;
					}
				});

		if (reqWidth == 0) {
			reqWidth = options.outWidth;
		}
		if (reqHeight == 0) {
			reqHeight = options.outHeight;
		}

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return bitmapConnection.readStream(urlString,
				new BitmapConnection.Runnable<Bitmap>() {
					@Override
					public Bitmap run(final InputStream stream) {
						return decodeStream(stream, options);
					}
				});
	}
}
