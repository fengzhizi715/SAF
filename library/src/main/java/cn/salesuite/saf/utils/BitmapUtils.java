/**
 * 
 */
package cn.salesuite.saf.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

/**
 * @author Tony Shen
 * 
 */
public class BitmapUtils {
    
    /**
     * 画圆角图形,默认的半径为10
	 *
	 * @param bitmap
	 * @return rounded corner bitmap
	 */
	public static Bitmap roundCorners(final Bitmap bitmap) {
		return roundCorners(bitmap, 10);
	}

	public static Bitmap roundCorners(final Bitmap bitmap, final int radius) {
		return roundCorners(bitmap, radius, radius);
	}

	public static Bitmap roundCorners(final Bitmap bitmap, final int radiusX,
			final int radiusY) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);

		Bitmap clipped = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(clipped);
		canvas.drawRoundRect(new RectF(0, 0, width, height), radiusX, radiusY,
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		Bitmap rounded = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		canvas = new Canvas(rounded);
		canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.drawBitmap(clipped, 0, 0, paint);

		return rounded;
	}
	
	public static Bitmap decodeBitmapFromResource(Bitmap.Config config, Resources res, int resId, int reqWidth, int reqHeight) {

	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	    options.inJustDecodeBounds = false;
	    options.inPreferredConfig = config;

	    return BitmapFactory.decodeResource(res, resId, options);
	}

	private static int calculateInSampleSize(
	            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) {
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	    }

	    return inSampleSize;
	}
}
