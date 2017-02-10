/**
 * 
 */
package cn.salesuite.saf.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import cn.salesuite.saf.executor.concurrent.BackgroundExecutor;

/**
 * 
 * @author Tony Shen
 * 
 */
public class EventBus {
    static ExecutorService executorService = new BackgroundExecutor();
    static ScheduledThreadPoolExecutor scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    
	public static final String DEFAULT_IDENTIFIER = "saf_bus";

	//所有注册的event handler
	private final ConcurrentMap<Class<?>, Set<EventHandler>> handlersByType = new ConcurrentHashMap<Class<?>, Set<EventHandler>>();

	//所有注册的event product
	private final ConcurrentMap<Class<?>, EventProducer> producersByType = new ConcurrentHashMap<Class<?>, EventProducer>();

	private final String identifier; // eventbus的名称

	// 当前线程分发的事件队列
	private final ThreadLocal<ConcurrentLinkedQueue<EventWithHandler>> eventsToDispatch = new ThreadLocal<ConcurrentLinkedQueue<EventWithHandler>>() {
		@Override
		protected ConcurrentLinkedQueue<EventWithHandler> initialValue() {
			return new ConcurrentLinkedQueue<EventWithHandler>();
		}
	};

	// 当前线程正在分发的事件，分发完成后设置为false
	private final ThreadLocal<Boolean> isDispatching = new ThreadLocal<Boolean>() {
		@Override
		protected Boolean initialValue() {
			return false;
		}
	};
	
    private final BackgroundPoster backgroundPoster;
    private final ScheduledPoster scheduledPoster;
	private final Map<Class<?>, Set<Class<?>>> flattenHierarchyCache = new HashMap<Class<?>, Set<Class<?>>>();

	@Override
	public String toString() {
		return "[\"" + identifier + "\"]";
	}

	public EventBus() {
		this(DEFAULT_IDENTIFIER);
	}

	public EventBus(String identifier) {
		this.identifier = identifier;
		backgroundPoster = new BackgroundPoster(this);
		scheduledPoster = new ScheduledPoster(this);
	}

	/**
	 * 注册到eventbus，支持activity、fragment
	 * @param object
	 */
	public void register(Object object) {

		Map<Class<?>, EventProducer> foundProducers = EventBusUtils
				.findAllProducers(object);
		for (Class<?> type : foundProducers.keySet()) {

			final EventProducer producer = foundProducers.get(type);
			EventProducer previousProducer = producersByType.putIfAbsent(type,
					producer);
			// checking if the previous producer existed
			if (previousProducer != null) {
				throw new IllegalArgumentException("Producer method for type "
						+ type + " found on type " + producer.target.getClass()
						+ ", but already registered by type "
						+ previousProducer.target.getClass() + ".");
			}
			Set<EventHandler> handlers = handlersByType.get(type);
			if (handlers != null && !handlers.isEmpty()) {
				for (EventHandler handler : handlers) {
					dispatchProducerResultToHandler(handler, producer);
				}
			}
		}

		Map<Class<?>, Set<EventHandler>> foundHandlersMap = EventBusUtils
				.findAllSubscribers(object);
		for (Class<?> type : foundHandlersMap.keySet()) {
			Set<EventHandler> handlers = handlersByType.get(type);
			if (handlers == null) {
				Set<EventHandler> handlersCreation = new CopyOnWriteArraySet<EventHandler>();
				handlers = handlersByType.putIfAbsent(type, handlersCreation);
				if (handlers == null) {
					handlers = handlersCreation;
				}
			}
			final Set<EventHandler> foundHandlers = foundHandlersMap.get(type);
			handlers.addAll(foundHandlers);
		}

		for (Map.Entry<Class<?>, Set<EventHandler>> entry : foundHandlersMap
				.entrySet()) {
			Class<?> type = entry.getKey();
			EventProducer producer = producersByType.get(type);
			if (producer != null && producer.isValid()) {
				Set<EventHandler> foundHandlers = entry.getValue();
				for (EventHandler foundHandler : foundHandlers) {
					if (!producer.isValid()) {
						break;
					}
					if (foundHandler.isValid()) {
						dispatchProducerResultToHandler(foundHandler, producer);
					}
				}
			}
		}
	}

	private void dispatchProducerResultToHandler(EventHandler handler,
			EventProducer producer) {
		Object event = null;
		try {
			event = producer.produceEvent();
		} catch (InvocationTargetException e) {
			throw new EventBusException("Producer " + producer
					+ " threw an exception.",e);
		}
		if (event == null) {
			return;
		}
		dispatch(event, handler);
	}

	/**
	 * 从eventbus移除
	 * @param object
	 */
	public void unregister(Object object) {

		Map<Class<?>, EventProducer> producersInListener = EventBusUtils
				.findAllProducers(object);
		for (Map.Entry<Class<?>, EventProducer> entry : producersInListener
				.entrySet()) {
			final Class<?> key = entry.getKey();
			EventProducer producer = getProducerForEventType(key);
			EventProducer value = entry.getValue();

			if (value == null || !value.equals(producer)) {
				throw new IllegalArgumentException(
						"Missing event producer for an annotated method. Is "
								+ object.getClass() + " registered?");
			}
			producersByType.remove(key).invalidate();
		}

		Map<Class<?>, Set<EventHandler>> handlersInListener = EventBusUtils
				.findAllSubscribers(object);
		for (Map.Entry<Class<?>, Set<EventHandler>> entry : handlersInListener
				.entrySet()) {
			Set<EventHandler> currentHandlers = getHandlersForEventType(entry
					.getKey());
			Collection<EventHandler> eventMethodsInListener = entry.getValue();

			if (currentHandlers == null
					|| !currentHandlers.containsAll(eventMethodsInListener)) {
				throw new IllegalArgumentException(
						"Missing event handler for an annotated method. Is "
								+ object.getClass() + " registered?");
			}

			for (EventHandler handler : currentHandlers) {
				if (eventMethodsInListener.contains(handler)) {
					handler.invalidate();
				}
			}
			currentHandlers.removeAll(eventMethodsInListener);
		}
	}

	/**
	 * 发送event，event发送成功后，订阅者可以在回调中处理
	 * 
	 * @param event
	 */
	public void post(Object event) {

		Set<Class<?>> dispatchTypes = flattenHierarchy(event.getClass());

		boolean dispatched = false;
		for (Class<?> eventType : dispatchTypes) {
			Set<EventHandler> wrappers = getHandlersForEventType(eventType);

			if (wrappers != null && !wrappers.isEmpty()) {
				dispatched = true;
				for (EventHandler wrapper : wrappers) {
					enqueueEvent(event, wrapper);
				}
			}
		}

		if (!dispatched && !(event instanceof DeadEvent)) {
			post(new DeadEvent(this, event));
		}

		dispatchQueuedEvents();
	}

	/**
	 * 事件进入队列
	 * @param event
	 * @param handler
	 */
	protected void enqueueEvent(Object event, EventHandler handler) {
		eventsToDispatch.get().offer(new EventWithHandler(event, handler));
	}

	/**
	 * 分发队列事件
	 */
	protected void dispatchQueuedEvents() {
		// 如果当前的事件已经在处理了，则不分发该事件
		if (isDispatching.get()) {
			return;
		}

		isDispatching.set(true);
		try {
			while (true) {
				EventWithHandler eventWithHandler = eventsToDispatch.get()
						.poll();
				if (eventWithHandler == null) {
					break;
				}

				if (eventWithHandler.handler.isValid()) {
					dispatch(eventWithHandler.event, eventWithHandler.handler);
				}
			}
		} finally {
			isDispatching.set(false);
		}
	}

	/**
	 * 根据订阅者的threadMode类型，分别处理event
	 * @param event
	 * @param wrapper
	 */
	protected void dispatch(Object event, EventHandler wrapper) {
		switch(wrapper.subscriberMethod.threadMode) {
		case PostThread:
			invokeSubscriber(event,wrapper);
			break;
		case BackgroundThread:
			backgroundPoster.enqueue(event,wrapper);
			break;
		case Async:
			break;
		case ScheduleBackgroundThread:
			if (event instanceof ScheduledEvent) {
				scheduledPoster.enqueue(event, wrapper);
			}
			
			break;
        default:
            throw new IllegalStateException("Unknown thread mode: " + wrapper.subscriberMethod.threadMode);
		}
	}
	
	/**
	 * 调用订阅者
	 * @param event
	 * @param wrapper
	 */
	void invokeSubscriber(Object event, EventHandler wrapper) {
		try {
			wrapper.handleEvent(event);
		} catch (InvocationTargetException e) {
			throw new EventBusException("Could not dispatch event: " + event.getClass()
							+ " to handler " + wrapper, e);
		}
	}

	EventProducer getProducerForEventType(Class<?> type) {
		return producersByType.get(type);
	}

	Set<EventHandler> getHandlersForEventType(Class<?> type) {
		return handlersByType.get(type);
	}

	Set<Class<?>> flattenHierarchy(Class<?> concreteClass) {
		Set<Class<?>> classes = flattenHierarchyCache.get(concreteClass);
		if (classes == null) {
			classes = getClassesFor(concreteClass);
			flattenHierarchyCache.put(concreteClass, classes);
		}

		return classes;
	}

	private Set<Class<?>> getClassesFor(Class<?> concreteClass) {
		List<Class<?>> parents = new LinkedList<Class<?>>();
		Set<Class<?>> classes = new HashSet<Class<?>>();

		parents.add(concreteClass);

		while (!parents.isEmpty()) {
			Class<?> clazz = parents.remove(0);
			classes.add(clazz);

			Class<?> parent = clazz.getSuperclass();
			if (parent != null) {
				parents.add(parent);
			}
		}
		return classes;
	}

	static class EventWithHandler {
		final Object event;
		final EventHandler handler;

		public EventWithHandler(Object event, EventHandler handler) {
			this.event = event;
			this.handler = handler;
		}
	}
}
