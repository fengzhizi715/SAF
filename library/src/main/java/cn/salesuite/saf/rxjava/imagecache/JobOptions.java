package cn.salesuite.saf.rxjava.imagecache;

import android.widget.ImageView;

/**
 * Created by Tony Shen on 15/11/13.
 */
public class JobOptions {

    public int cornerRadius = 5;
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
