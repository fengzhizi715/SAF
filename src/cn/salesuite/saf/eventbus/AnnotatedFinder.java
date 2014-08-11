/**
 * 
 */
package cn.salesuite.saf.eventbus;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 查找注解 @Produce 和 @Subscribe
 * @author Tony Shen
 *
 */
final class AnnotatedFinder {

	  /** Cache event bus producer methods for each class. */
	  private static final Map<Class<?>, Map<Class<?>, Method>> PRODUCERS_CACHE =
	      new HashMap<Class<?>, Map<Class<?>, Method>>();

	  /** Cache event bus subscriber methods for each class. */
	  private static final Map<Class<?>, Map<Class<?>, Set<SubscriberMethod>>> SUBSCRIBERS_CACHE =
	      new HashMap<Class<?>, Map<Class<?>, Set<SubscriberMethod>>>();

	  /**
	   * Load all methods annotated with {@link Produce} or {@link Subscribe} into their respective caches for the
	   * specified class.
	   */
	  private static void loadAnnotatedMethods(Class<?> listenerClass) {
	    Map<Class<?>, Set<SubscriberMethod>> subscriberMethods = new HashMap<Class<?>, Set<SubscriberMethod>>();
	    Map<Class<?>, Method> producerMethods = new HashMap<Class<?>, Method>();

	    for (Method method : listenerClass.getDeclaredMethods()) {
	      if (method.isAnnotationPresent(Subscribe.class)) {
	        Class<?>[] parameterTypes = method.getParameterTypes();
	        if (parameterTypes.length != 1) {
	          throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation but requires "
	              + parameterTypes.length + " arguments.  Methods must require a single argument.");
	        }

	        Class<?> eventType = parameterTypes[0];
	        if (eventType.isInterface()) {
	          throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + eventType
	              + " which is an interface.  Subscription must be on a concrete class type.");
	        }

	        if ((method.getModifiers() & Modifier.PUBLIC) == 0) {
	          throw new IllegalArgumentException("Method " + method + " has @Subscribe annotation on " + eventType
	              + " but is not 'public'.");
	        }

	        Set<SubscriberMethod> methods = subscriberMethods.get(eventType);
	        if (methods == null) {
	          methods = new HashSet<SubscriberMethod>();
	          subscriberMethods.put(eventType, methods);
	        }

	        SubscriberMethod subscriberMethod = new SubscriberMethod(method,(method.getAnnotation(Subscribe.class)).value());
	        methods.add(subscriberMethod);
	      } else if (method.isAnnotationPresent(Produce.class)) {
	        Class<?>[] parameterTypes = method.getParameterTypes();
	        if (parameterTypes.length != 0) {
	          throw new IllegalArgumentException("Method " + method + "has @Produce annotation but requires "
	              + parameterTypes.length + " arguments.  Methods must require zero arguments.");
	        }
	        if (method.getReturnType() == Void.class) {
	          throw new IllegalArgumentException("Method " + method
	              + " has a return type of void.  Must declare a non-void type.");
	        }

	        Class<?> eventType = method.getReturnType();
	        if (eventType.isInterface()) {
	          throw new IllegalArgumentException("Method " + method + " has @Produce annotation on " + eventType
	              + " which is an interface.  Producers must return a concrete class type.");
	        }
	        if (eventType.equals(Void.TYPE)) {
	          throw new IllegalArgumentException("Method " + method + " has @Produce annotation but has no return type.");
	        }

	        if ((method.getModifiers() & Modifier.PUBLIC) == 0) {
	          throw new IllegalArgumentException("Method " + method + " has @Produce annotation on " + eventType
	              + " but is not 'public'.");
	        }

	        if (producerMethods.containsKey(eventType)) {
	          throw new IllegalArgumentException("Producer for type " + eventType + " has already been registered.");
	        }
	        producerMethods.put(eventType, method);
	      }
	    }

	    PRODUCERS_CACHE.put(listenerClass, producerMethods);
	    SUBSCRIBERS_CACHE.put(listenerClass, subscriberMethods);
	  }

	  /** This implementation finds all methods marked with a {@link Produce} annotation. */
	  static Map<Class<?>, EventProducer> findAllProducers(Object listener) {
	    final Class<?> listenerClass = listener.getClass();
	    Map<Class<?>, EventProducer> handlersInMethod = new HashMap<Class<?>, EventProducer>();

	    if (!PRODUCERS_CACHE.containsKey(listenerClass)) {
	      loadAnnotatedMethods(listenerClass);
	    }
	    Map<Class<?>, Method> methods = PRODUCERS_CACHE.get(listenerClass);
	    if (!methods.isEmpty()) {
	      for (Map.Entry<Class<?>, Method> e : methods.entrySet()) {
	        EventProducer producer = new EventProducer(listener, e.getValue());
	        handlersInMethod.put(e.getKey(), producer);
	      }
	    }

	    return handlersInMethod;
	  }

	  /** This implementation finds all methods marked with a {@link Subscribe} annotation. */
	  static Map<Class<?>, Set<EventHandler>> findAllSubscribers(Object listener) {
	    Class<?> listenerClass = listener.getClass();
	    Map<Class<?>, Set<EventHandler>> handlersInMethod = new HashMap<Class<?>, Set<EventHandler>>();

	    if (!SUBSCRIBERS_CACHE.containsKey(listenerClass)) {
	      loadAnnotatedMethods(listenerClass);
	    }
	    Map<Class<?>, Set<SubscriberMethod>> methods = SUBSCRIBERS_CACHE.get(listenerClass);
	    if (!methods.isEmpty()) {
	      for (Map.Entry<Class<?>, Set<SubscriberMethod>> e : methods.entrySet()) {
	        Set<EventHandler> handlers = new HashSet<EventHandler>();
	        for (SubscriberMethod m : e.getValue()) {
	          handlers.add(new EventHandler(listener, m));
	        }
	        handlersInMethod.put(e.getKey(), handlers);
	      }
	    }

	    return handlersInMethod;
	  }

	  private AnnotatedFinder() {
	    // No instances.
	  }
}
