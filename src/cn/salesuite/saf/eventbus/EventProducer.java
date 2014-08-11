/*
 */

package cn.salesuite.saf.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 * @author Tony Shen
 * 
 */
class EventProducer {

  final Object target;

  private final Method method;

  private final int hashCode;

  private boolean valid = true;

  EventProducer(Object target, Method method) {
    if (target == null) {
      throw new NullPointerException("EventProducer target cannot be null.");
    }
    if (method == null) {
      throw new NullPointerException("EventProducer method cannot be null.");
    }

    this.target = target;
    this.method = method;
    method.setAccessible(true);

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
   * 返回event
   * @return
   * @throws InvocationTargetException
   */
  public Object produceEvent() throws InvocationTargetException {
    if (!valid) {
      throw new IllegalStateException(toString() + " has been invalidated and can no longer produce events.");
    }
    try {
      return method.invoke(target);
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
    return "[EventProducer " + method + "]";
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

    final EventProducer other = (EventProducer) obj;

    return method.equals(other.method) && target == other.target;
  }
}
