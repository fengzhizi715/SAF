/**
 * 
 */
package cn.salesuite.saf.imagecache;

import java.io.IOException;
import java.io.InputStream;

import cn.salesuite.saf.http.rest.RestClient;

/**
 * @author Tony Shen
 *
 */
class BitmapConnection {
	public interface Runnable<T> {
		T run(InputStream stream);
	}

	public <T> T readStream(String urlString,Runnable<T> runnable) {		
		RestClient client = RestClient.get(urlString).useCaches(true);
		InputStream inputStream = new PatchInputStream(client.stream());
		try {
			return runnable.run(inputStream);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}

	}
}
