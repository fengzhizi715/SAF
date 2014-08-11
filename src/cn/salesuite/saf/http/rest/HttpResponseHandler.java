/**
 * 
 */
package cn.salesuite.saf.http.rest;

/**
 * @author Tony Shen
 *
 */
public interface HttpResponseHandler {

	/**
	 * http请求成功后，response转换成content
	 * @param content
	 */
	public void onSuccess(String content);
}
