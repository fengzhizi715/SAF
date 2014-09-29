package cn.salesuite.saf.inject;

import java.lang.reflect.Method;

import cn.salesuite.saf.log.L;

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
		L.d(this.getClass().getSimpleName() +".target[" + target.getClass().getName() + "].method[" + method.getName() + "]");
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
