/*
 */
package cn.salesuite.saf.eventbus;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Tony Shen
 * 
 */
class EventHandler {

  final Object target;

  final SubscriberMethod subscriberMethod;

  private final int hashCode;

  private boolean valid = true;

  EventHandler(Object target, SubscriberMethod method) {
    if (target == null) {
      throw new NullPointerException("EventHandler target cannot be null.");
    }
    if (method == null) {
      throw new NullPointerException("EventHandler method cannot be null.");
    }

    this.target = target;
    this.subscriberMethod = method;
    method.method.setAccessible(true);

    final int prime = 31;
    hashCode = (prime + method.hashCode()) * prime + target.hashCode();
  }

  public boolean isValid() {
    return valid;
  }

  public void invalidate() {
    valid = false;
  }

  /**
   * 处理event
   * @param event
   * @throws InvocationTargetException
   */
  public void handleEvent(Object event) throws InvocationTargetException {
    if (!valid) {
      throw new IllegalStateException(toString() + " has been invalidated and can no longer handle events.");
    }
    try {
      subscriberMethod.method.invoke(target, event);
    } catch (IllegalAccessException e) {
      throw new AssertionError(e);
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof Error) {
        throw (Error) e.getCause();
      }
      throw e;
    }
  }

  @Override 
  public String toString() {
    return "[EventHandler " + subscriberMethod + "]";
  }

  @Override 
  public int hashCode() {
    return hashCode;
  }

  @Override 
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    final EventHandler other = (EventHandler) obj;

    return subscriberMethod.equals(other.subscriberMethod) && target == other.target;
  }

}
