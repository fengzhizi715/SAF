/**
 * 
 */
package cn.salesuite.saf.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.alibaba.fastjson.JSON;

/**
 * 可以发送json消息实体的httpclient
 * @author Tony Shen
 *
 * 
 */
@Deprecated
public class HttpJsonPost extends HttpJsonClient{

    public HttpJsonPost() {
    	this("HttpJsonPost");
    }
    
    public HttpJsonPost(String TAG) {
        super(TAG);
    }
	
	/**
	 * 创建http请求
	 * @param url
	 * @param customizedHeader
	 * @param entity  a raw {@link HttpEntity} to send with the request, for example, use this to send json payloads to a server by passing a {@link org.apache.http.entity.StringEntity}.
	 * @param callback
	 * @throws IOException
	 */
	public void makeHTTPRequest(String url,Map<String, String> customizedHeader,HttpEntity entity,
			OnResponseReceivedListener callback)
			throws  IOException {
		Log.d("URL",url);
		 
		if(customizedHeader==null){
			customizedHeader = new HashMap<String, String>();
		}
		customizedHeader.put("Accept-Encoding", "gzip");
		customizedHeader.put("Connection" , "Keep-Alive");
		customizedHeader.put("Content-Type" , "application/json;charset=utf-8");
		customizedHeader.put("Accept" , "application/json");
		
		HttpPost httpRequest = createHttpPost(url, customizedHeader);
		if (entity!=null) {
			Log.i(TAG,"post body="+EntityUtils.toString(entity));
			httpRequest.setEntity(entity);
		}
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
	
    public StringEntity objToEntity(Object jsonObj) throws IOException {
    	String json = JSON.toJSONString(jsonObj);
        return stringToEntity(json);
    }
    
    public StringEntity stringToEntity(String json) throws IOException {
    	return new StringEntity(json, "utf-8");
    }

	private void executeResponseCallback(
			final OnResponseReceivedListener callback, final InputStream response) {
			callback.onResponseReceived(response);
	}
}