/**
 * 
 */
package cn.salesuite.saf.http.rest;

/**
 * http重试机制的策略
 * @author Tony Shen
 *
 */
public interface HttpRetryStrategy {

	/**
	 * 执行重试的逻辑
	 */
	public void retry();
}
