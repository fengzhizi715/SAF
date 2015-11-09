/**
 * 
 */
package cn.salesuite.saf.imagecache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author Tony Shen
 *
 */
public class ImageResponseCache extends ResponseCache{
	private final File cacheDir;

    public ImageResponseCache(final File cacheDir) {
        this.cacheDir = cacheDir;
    }

    @Override
    public CacheResponse get(final URI uri, final String s, final Map<String, List<String>> headers) throws IOException {
        final File file = new File(cacheDir, escape(uri.getPath()));
        if (file.exists())
            return new CacheResponse() {

                @Override
                public Map<String, List<String>> getHeaders() throws IOException {
                    return null;
                }

                @Override
                public InputStream getBody() throws IOException {
                    return new FileInputStream(file);
                }
            };
        else
            return null;
    }

    @Override
    public CacheRequest put(final URI uri, final URLConnection urlConnection) throws IOException {
        final File file = new File(cacheDir, escape(urlConnection.getURL().getPath()));
        return new CacheRequest() {

            @Override
            public OutputStream getBody() throws IOException {
                return new FileOutputStream(file);
            }

            @Override
            public void abort() {
                file.delete();
            }
        };
    }

    private String escape(final String url) {
        return url.replace("/", "-").replace(".", "-");
    }
}
