/**
 * 
 */
package cn.salesuite.saf.http.rest;

/**
 * @author Hendry.cao
 * @param <T>
 *
 */
public interface BinaryResponseHandler {
	/**
	 * http请求 初始化
	 * @param total
	 */
	public void onLoading(int total);
	/**
	 * http请求失败，response转换成jsonString
	 * 
	 * @param e
	 */
	public void onFail(RestException e);
	
	/**
	 * http请求成功后，response转换成byte
	 * @param content
	 */
	public void onSuccess(byte[] content);
	
	/**
	 * http请求成功后，输出下载进度值
	 * @param current
	 * @param total
	 */
	public void onProgress(int current);
	
}
