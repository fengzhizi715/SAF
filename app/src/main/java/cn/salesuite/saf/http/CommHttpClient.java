/**
 * 
 */
package cn.salesuite.saf.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.text.TextUtils;
import android.util.Log;

/**
 * 使用CommHttpClient时，可以访问http/https类型的url,支持http gzip压缩的方式传输<br>
 * @author Tony Shen
 *
 */
@Deprecated
public class CommHttpClient {
	
	public String TAG = "CommHttpClient";
	public static final int ONE_MINUTE = 60000;
    private static final int DEFAULT_MAX_RETRIES = 5;
	public HttpClient httpClient;
	
	public CommHttpClient() {
		httpClient = createHttpClient();
	}
	
	public CommHttpClient(String TAG) {
		httpClient = createHttpClient();
		this.TAG = TAG;
	}
	
	/**
	 * 创建httpclient对象,支持访问http/https的url
	 * @return a working instance of an HttpClient
	 */
	private HttpClient createHttpClient() {
		HttpParams params = createHttpParams();
		
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		try {
			MySSLSocketFactory sslFactory = new MySSLSocketFactory(null);
			sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			registry.register(new Scheme("https", sslFactory, 443));
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

		ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(
				params, registry);
		DefaultHttpClient httpClient = new DefaultHttpClient(manager, params);
		httpClient.setHttpRequestRetryHandler(new RetryHandler(DEFAULT_MAX_RETRIES));
		
		return httpClient;
	}
	
	private HttpParams createHttpParams() {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
		HttpConnectionParams.setConnectionTimeout(httpParams, ONE_MINUTE);
		HttpConnectionParams.setSoTimeout(httpParams, ONE_MINUTE);
		HttpConnectionParams.setSocketBufferSize(httpParams, 1024*2);
		return httpParams;
	}

	/**
	 * 创建http/https请求
	 * @param url 完整的url链接，包括需要传递的参数
	 * @param customizedHeader
	 * @param callback
	 * @throws IOException
	 */
	public void makeHTTPRequest(String url, 
			Map<String, String> customizedHeader,
			OnResponseReceivedListener callback) 
	        throws  IOException {
		Log.d("URL", url);
		 
		if(customizedHeader==null){
			customizedHeader = new HashMap<String, String>();
		}
		customizedHeader.put("Accept-Encoding", "gzip");
		customizedHeader.put("Connection" , "Keep-Alive");
		
		HttpPost httpRequest = createHttpPost(url, customizedHeader);
		HttpResponse httpResponse = executeHttpRequest(httpRequest);
		HttpEntity httpEntity = httpResponse.getEntity();
		
		//success
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
			//String response = EntityUtils.toString(httpEntity);
			InputStream instream = httpEntity.getContent();
			if (contentEncoding!=null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				instream = new GZIPInputStream(instream);
				executeResponseCallback(callback, instream);
			} else {
				executeResponseCallback(callback, instream);
			}
		} else {
			Log.e("error", "Request failure! url:"+url);
			
			if(httpEntity != null)
				httpEntity.consumeContent();
		}

		httpClient.getConnectionManager().shutdown();
	}
	
	/**
	 * 创建http/https请求
	 * @param url 接口地址
	 * @param postdata 把需要传递的参数封装到Map对象
	 * @param customizedHeader
	 * @param callback
	 * @throws IOException
	 */
	public void makeHTTPRequest(String url,
			Map<String, String> postdata, Map<String, String> customizedHeader,
			OnResponseReceivedListener callback)
			throws  IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(url).append("?");
		for (Map.Entry<String, String> item : postdata.entrySet()) {
			sb.append("").append(item.getKey()).append("=").append(
					item.getValue()).append("&");
		}
		Log.d("URL", sb.toString());
		 
		if(customizedHeader==null){
			customizedHeader = new HashMap<String, String>();
		}
		customizedHeader.put("Accept-Encoding", "gzip");
		customizedHeader.put("Connection" , "Keep-Alive");
		
		HttpPost httpRequest = createHttpPost(url, postdata, customizedHeader);
		HttpResponse httpResponse = executeHttpRequest(httpRequest);
		HttpEntity httpEntity = httpResponse.getEntity();
		
		//success
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
			//String response = EntityUtils.toString(httpEntity);
			InputStream instream = httpEntity.getContent();
			if (contentEncoding!=null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				instream = new GZIPInputStream(instream);
				executeResponseCallback(callback, instream);
			} else {
				executeResponseCallback(callback, instream);
			}
		} else {
			Log.e("error", "Request failure! url:"+url);
			
			if(httpEntity != null)
				httpEntity.consumeContent();
		}

		httpClient.getConnectionManager().shutdown();
	}
	
	public HttpPost createHttpPost(String url,Map<String, String> customizedHeader) {
		HttpPost httpRequest = new HttpPost(url);

		if(customizedHeader != null) {
			for (Map.Entry<String, String> item : customizedHeader.entrySet()) {
				Header header = new BasicHeader(item.getKey(), item.getValue());
				httpRequest.addHeader(header);
				Log.i(TAG, item.getKey()+"="+item.getValue());
			}
		}

		return httpRequest;
	}
	
	public HttpPost createHttpPost(String url, Map<String, String> postdata, Map<String, String> customizedHeader) {
		HttpPost httpRequest = new HttpPost(url);
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		if(postdata != null) {
			for (Map.Entry<String, String> item : postdata.entrySet()) {
				params.add(new BasicNameValuePair(item.getKey(), item.getValue()));
				Log.d(TAG, item.getKey()+"="+item.getValue());
			}
		}
		if(customizedHeader != null) {
			for (Map.Entry<String, String> item : customizedHeader.entrySet()) {
				Header header = new BasicHeader(item.getKey(), item.getValue());
				httpRequest.addHeader(header);
				Log.i(TAG, item.getKey()+"="+item.getValue());
			}
		}
		try {
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unable to encode http parameters.");
		}
		return httpRequest;
	}
	
	public HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws IOException {
		Log.d(TAG, "executing HttpRequest for:" + httpRequest.getURI().toString());
		
		try {
			return httpClient.execute(httpRequest);
		} catch (IOException e) {
			httpRequest.abort();
			throw e;
		}
	}
	
	/**
	 * 把所有的非空传入参数，通过a-z排序后，
	 * 使用key=value&key=value连接
	 * @param params
	 * @return
	 */
	public static String sortParams(Map<String,String> params) {
		StringBuilder sb = new StringBuilder();
		String str = null;
		
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> mRemoveList = new ArrayList<String>();

		for (Map.Entry<String, String> item : params.entrySet()) {
			if(item.getValue()==null || item.getValue().trim().length()==0
					|| "null".equals(item.getValue())){
				mRemoveList.add(item.getKey());
			}
			list.add(item.getKey());
		}
		
		int removeCount = mRemoveList.size();
		for(int i=0;i<removeCount;i++){
			params.remove(mRemoveList.get(i));
			list.remove(mRemoveList.get(i));
		} 
		Collections.sort(list);
		
		for(int i=0;i<list.size();i++)
		{
			String key = list.get(i);
			String value = "";
			if(!TextUtils.isEmpty(key)){
				value = params.get(key); 
				sb.append(key).append("=").append(value).append("&"); 
			} 
		}
		str = sb.toString();
		if(!TextUtils.isEmpty(str)){
			str = str.substring(0, str.length()-1);
		}

		return str;
	}
	
	private void executeResponseCallback(
			final OnResponseReceivedListener callback, final InputStream response) {
			callback.onResponseReceived(response);
	}
	
	public interface OnResponseReceivedListener{
		public void onResponseReceived(InputStream response);
	}
}