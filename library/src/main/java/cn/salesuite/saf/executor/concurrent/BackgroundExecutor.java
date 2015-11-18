/**
 * 
 */
package cn.salesuite.saf.executor.concurrent;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Tony Shen
 * 
 */
public class BackgroundExecutor extends ThreadPoolExecutor {
	
	public BackgroundExecutor() {
		super(0, Integer.MAX_VALUE, 60L,
				TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
				new BackgroundPriorityThreadFactory());
	}

	public BackgroundExecutor(int nThreads) {
		super(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(),
				new BackgroundPriorityThreadFactory());
	}
}
