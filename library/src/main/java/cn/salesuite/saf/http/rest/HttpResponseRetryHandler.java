/**
 * 
 */
package cn.salesuite.saf.http.rest;


/**
 * 带http重试机制的handler
 * @author Tony Shen
 *
 */
public abstract class HttpResponseRetryHandler implements HttpResponseHandler,HttpRetryStrategy{
	
	private int retryNum = RestConstant.DEFAULT_RETRY_NUM;
	
	public HttpResponseRetryHandler(){
	}
	
	public HttpResponseRetryHandler(int retryNum){
		this.retryNum = retryNum;
	}
	
	public int getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(int retryNum) {
		this.retryNum = retryNum;
	}
}
