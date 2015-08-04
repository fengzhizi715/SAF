/**
 * 
 */
package cn.salesuite.saf.imagecache;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import cn.salesuite.saf.download.AndroidHttpClient;
import cn.salesuite.saf.http.rest.RestClient;
import cn.salesuite.saf.utils.SAFUtils;

/**
 * @author Tony Shen
 *
 */
class BitmapConnection {
	public interface Runnable<T> {
		T run(InputStream stream);
	}

	public <T> T readStream(String urlString,Runnable<T> runnable) {
		InputStream inputStream = null;
		if (SAFUtils.isLOrHigher()) {
			AndroidHttpClient client = AndroidHttpClient.newInstance("BitmapConnection");
			HttpGet httpGet = new HttpGet(urlString);
			
			HttpResponse response;
			try {
				response = client.execute(httpGet);
				inputStream = new PatchInputStream(response.getEntity().getContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			RestClient client = RestClient.get(urlString).useCaches(true);
			inputStream = new PatchInputStream(client.stream());
		}
		
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
