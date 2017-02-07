/**
 * 
 */
package cn.salesuite.saf.executor.concurrent;

import android.os.Process;

import com.safframwork.tony.common.utils.Preconditions;

import java.util.concurrent.ThreadFactory;

/**
 * 使用该类时，app需要增加权限：&ltuses-permission android:name="android.permission.RAISED_THREAD_PRIORITY"/>
 * @author Tony Shen
 *
 */
public class BackgroundPriorityThreadFactory implements ThreadFactory {
	
	public String threadName;
	
	public BackgroundPriorityThreadFactory() {
	}
	
	public BackgroundPriorityThreadFactory(String threadName) {
		this.threadName = threadName;
	}

	@Override
	public Thread newThread(Runnable r) {
		
		if (Preconditions.isNotBlank(threadName)) {
			return new Thread(r,threadName) {
				@Override
				public void run() {
					Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
					super.run();
				}
			};
		} else {
			return new Thread(r) {
				@Override
				public void run() {
					Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
					super.run();
				}
			};
		}
		
	}
}
