/**
 * 
 */
package cn.salesuite.saf.eventbus;

import java.util.concurrent.TimeUnit;

/**
 * @author Tony Shen
 *
 */
public class ScheduledEvent {

	public long initialDelay;
	public long period;
	public TimeUnit unit = TimeUnit.MILLISECONDS;
}
