/**
 * 
 */
package cn.salesuite.saf.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import cn.salesuite.saf.async.AsyncTask;
import android.annotation.SuppressLint;

/**
 * @author Tony Shen
 *
 */
public class AsyncTaskExecutor {

	private static final int CORE_POOL_SIZE;
	private static final int MAXIMUM_POOL_SIZE;
	private static final int KEEP_ALIVE;
	private static final TimeUnit TIME_UNIT;

	private static final BlockingQueue<Runnable> concurrentPoolWorkQueue;
	private static final ThreadFactory concurrentThreadFactory;
    private static final ThreadPoolExecutor concurrentExecutor;
    
    private AsyncTaskExecutor() {}
    
    static {
    	CORE_POOL_SIZE = 5;
    	MAXIMUM_POOL_SIZE = 128;
    	KEEP_ALIVE = 1;
    	TIME_UNIT = TimeUnit.SECONDS;
    	
    	concurrentPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);
    	concurrentThreadFactory = new AsyncTaskThreadFactory();
    	concurrentExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, 
				MAXIMUM_POOL_SIZE, KEEP_ALIVE, TIME_UNIT, concurrentPoolWorkQueue, concurrentThreadFactory);
    }
    
    /**
     * Concurrently executes AsyncTask on any Android version
     * @param task to execute
     * @param params for task
     * @return executing AsyncTask 
     */
    @SuppressLint("NewApi") 
    public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> 
    executeAsyncTask(AsyncTask<Params, Progress, Result> task,
    		Params... params) {
    	if (SAFUtils.isHoneycombOrHigher()) {
    		task.executeOnExecutor(concurrentExecutor, params);
    	} else {
    		task.execute(params);
    	}
    	return task;
    }
    
    /**
     * Concurrently executes AsyncTask on any Android version
     * @param task to execute
     * @param params for task
     * @return executing AsyncTask 
     */
    @SuppressLint("NewApi") 
    public static <Params, Progress, Result> android.os.AsyncTask<Params, Progress, Result> 
    executeAsyncTask(android.os.AsyncTask<Params, Progress, Result> task,
    		Params... params) {
    	if (SAFUtils.isHoneycombOrHigher()) {
    		task.executeOnExecutor(concurrentExecutor, params);
    	} else {
    		task.execute(params);
    	}
    	return task;
    }

    private static class AsyncTaskThreadFactory implements ThreadFactory {
    	private final AtomicInteger count;
    	
    	{
    		count = new AtomicInteger(1);
    	}
    	
		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask #" + count.getAndIncrement());
		}
    }
}
