/**
 * 
 */
package cn.salesuite.saf.eventbus;

import java.lang.reflect.Method;

/**
 * 订阅者的方法，封装了Method和ThreadMode
 * @author Tony Shen
 *
 */
final class SubscriberMethod {
	
	final Method method;
    final ThreadMode threadMode;
    
    /** Used for efficient comparison */
    String methodString;

    SubscriberMethod(Method method, ThreadMode threadMode) {
        this.method = method;
        this.threadMode = threadMode;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof SubscriberMethod) {
            checkMethodString();
            return methodString.equals(((SubscriberMethod) other).methodString);
        } else {
            return false;
        }
    }

    private synchronized void checkMethodString() {
        if (methodString == null) {
            StringBuilder builder = new StringBuilder(64);
            builder.append(method.getDeclaringClass().getName());
            builder.append('#').append(method.getName());
            methodString = builder.toString();
        }
    }

    @Override
    public int hashCode() {
        return method.hashCode();
    }
}
