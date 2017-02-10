/**
 * 
 */
package cn.salesuite.saf.eventbus;

/**
 * EventBus不处理DeadEvent
 * @author Tony Shen
 *
 */
public class DeadEvent {

	public final Object source;
	public final Object event;

	public DeadEvent(Object source, Object event) {
		this.source = source;
		this.event = event;
	}
}
