/**
 * 
 */
package cn.salesuite.saf.executor.concurrent;

import java.util.concurrent.ThreadFactory;

import android.os.Process;

/**
 * 使用该类时，app需要增加权限：&ltuses-permission android:name="android.permission.RAISED_THREAD_PRIORITY"/>
 * @author Tony Shen
 *
 */
public class BackgroundPriorityThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable r) {
		return new Thread(r) {
			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				super.run();
			}
		};
	}
}
