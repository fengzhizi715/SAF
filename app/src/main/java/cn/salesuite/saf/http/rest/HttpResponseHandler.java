/**
 * 
 */
package cn.salesuite.saf.http.rest;

import java.util.List;
import java.util.Map;

/**
 * @author Tony Shen
 *
 */
public interface HttpResponseHandler {
	
	/**
	 * http请求成功后，response转换成content
	 * @param content
	 * @param heads
	 */
	public void onSuccess(String content,Map<String, List<String>> heads);
	
	/**
	 * http请求失败后，response转换成jsonString
	 * 
	 * @param e
	 */
	public void onFail(RestException e);
	
}
