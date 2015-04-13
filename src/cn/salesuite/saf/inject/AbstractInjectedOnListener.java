package cn.salesuite.saf.inject;

import android.view.View;

import java.lang.reflect.Method;

import cn.salesuite.saf.log.L;
import cn.salesuite.saf.utils.StringUtils;

/**
 * @author frankswu
 *
 */
public abstract class AbstractInjectedOnListener {
	
    protected final Object target;
    protected final Method method;
    protected final boolean invokeWithViewParam;
	protected static boolean enabled = true;

    protected  boolean hasBeforeMethodName = false;
    protected  boolean hasAfterMethodName = false;
    protected String beforeMethodName;
    protected String afterMethodName;

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

    AbstractInjectedOnListener(Object target, Method method,boolean invokeWithViewParam, String beforeMethodName, String afterMethodName) {
        this.target = target;
        this.method = method;
        this.invokeWithViewParam = invokeWithViewParam;
        if (StringUtils.isNotBlank(beforeMethodName)) {
            hasBeforeMethodName = true;
            this.beforeMethodName = beforeMethodName;
        }
        
        if (StringUtils.isNotBlank(afterMethodName)) {
            hasAfterMethodName = true;
            this.afterMethodName = afterMethodName;
        }
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

    protected void invokeMethod(boolean hasMethod, String methodName, Method method,View v) {
        invokeMethod(hasMethod,methodName,method,v,-1,-1);
    }

    protected void invokeMethod(boolean hasMethod, String methodName, Method method,View v, int postion, long id) {
        L.d(this.getClass().getSimpleName() +".invokeMethod.hasMethod[" + hasMethod + "].methodName[" + methodName + "]");
        if (hasMethod) {
            Method[] methods = method.getDeclaringClass().getDeclaredMethods();
            for (Method m : methods) {
                if (methodName.equals(m.getName())) {
                    try {
                        m.setAccessible(true);
                        if (postion == -1) {
                            m.invoke(target,method,v);
                        } else {
                            m.invoke(target,method,v, postion, id);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        L.e("InjectedOnClickListener before or after method[" + methodName + "] invoke is error");
                    }
                }
            }
        }
    }
}
