/**
 * 
 */
package cn.salesuite.saf.imagecache;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream patch for issue in BitmapFactory.decodeStream:
 * http://code.google.com/p/android/issues/detail?id=6066
 * @author Tony Shen
 *
 */
public class PatchInputStream extends FilterInputStream {
	
	public PatchInputStream(final InputStream in) {
        super(in);
    }

    @Override
    public long skip(final long n) throws IOException {
        long totalBytesSkipped = 0L;
        while (totalBytesSkipped < n) {
            long bytesSkipped = in.skip(n - totalBytesSkipped);
            if (bytesSkipped == 0L) {
                final int bt = read();
                if (bt < 0) {
                    break;  // we reached EOF
                } else {
                    bytesSkipped = 1; // we read one byte
                }
            }
            totalBytesSkipped += bytesSkipped;
        }
        return totalBytesSkipped;
    }
}
