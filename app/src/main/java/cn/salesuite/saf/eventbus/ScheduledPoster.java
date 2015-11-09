/**
 * 
 */
package cn.salesuite.saf.eventbus;


/**
 * @author Tony Shen
 *
 */
public class ScheduledPoster {

	private final EventBus eventBus;

	ScheduledPoster(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void enqueue(final Object event,final EventHandler subscription) {
		
		ScheduledEvent scheduledEvent = (ScheduledEvent)event;
		EventBus.scheduledExecutorService.scheduleAtFixedRate(new Runnable(){

			@Override
			public void run() {
				eventBus.invokeSubscriber(event,subscription);
			}
			
		},scheduledEvent.initialDelay,scheduledEvent.period,scheduledEvent.unit);
	}
}
