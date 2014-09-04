package cn.salesuite.saf.inject;

import java.lang.reflect.Method;

/**
 * @author frankswu
 *
 */
public abstract class AbstractInjectedOnListener {
	
    protected final Object target;
    protected final Method method;
    protected final boolean invokeWithViewParam;
	protected static boolean enabled = true;

	protected static final Runnable ENABLE_AGAIN = new Runnable() {
		@Override
		public void run() {
			enabled = true;
		}
	};

	AbstractInjectedOnListener(Object target, Method method,boolean invokeWithViewParam) {
		this.target = target;
		this.method = method;
		this.invokeWithViewParam = invokeWithViewParam;
	}
	  
	protected void handleOnListener(Object... objs) {
        try {
            if (invokeWithViewParam) {
                method.invoke(target, objs);
            } else {
                method.invoke(target);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
}
