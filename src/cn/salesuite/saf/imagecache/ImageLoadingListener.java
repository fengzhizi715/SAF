package cn.salesuite.saf.imagecache;

import android.graphics.Bitmap;
import android.view.View;

/**
 * User: Aaron.Liu
 * Date: 14-10-11
 * Time: 17:29
 */
public interface ImageLoadingListener {


    /**
     * Is called when image is loaded successfully (and displayed in View if one was specified)
     *
     * @param imageUri    Loaded image URI
     * @param loadedImage Bitmap of loaded and decoded image
     */
    void onLoadingComplete(String imageUri, Bitmap loadedImage);

    /**
     * Is called when image loading task was cancelled because View for image was reused in newer task
     *
     * @param imageUri Loading image URI
     * @param view     View for image. Can be <b>null</b>.
     */
    void onLoadingCancelled(String imageUri, View view);
}