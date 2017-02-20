/**
 * 
 */
package com.safframework.saf.rest;

/**
 * http重试机制的策略
 * @author Tony Shen
 *
 */
public interface HttpRetryStrategy {

	/**
	 * 执行重试的逻辑,这些逻辑往往是比较特别的
	 */
	public void retry();
}
