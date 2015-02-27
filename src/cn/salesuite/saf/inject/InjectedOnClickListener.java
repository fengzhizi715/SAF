/**
 * 
 */
package cn.salesuite.saf.inject;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnClickListener;

import cn.salesuite.saf.eventbus.EventBus;
import cn.salesuite.saf.log.L;

/**
 * @author Tony Shen
 *
 */
public class InjectedOnClickListener extends AbstractInjectedOnListener implements OnClickListener {

    protected  boolean hasBeforeMethodName = false;
    protected  boolean hasAfterMethodName = false;
    protected String beforeMethodName;
    protected String afterMethodName;

	InjectedOnClickListener(Object target, Method method,boolean invokeWithViewParam) {
		super(target, method, invokeWithViewParam);
	}

    InjectedOnClickListener(Object target, Method method,boolean invokeWithViewParam, String beforeMethodName, String afterMethodName) {
        super(target, method, invokeWithViewParam);
        if (beforeMethodName == null || "".equals(beforeMethodName)) {
            hasBeforeMethodName = true;
            this.beforeMethodName = beforeMethodName;
        }
        if (afterMethodName == null || "".equals(afterMethodName)) {
            hasAfterMethodName = true;
            this.afterMethodName = afterMethodName;
        }
    }


	@Override
	public final void onClick(View v) {
		if (enabled) {
			enabled = false;
			v.post(ENABLE_AGAIN);
            invokeMethod(hasBeforeMethodName,beforeMethodName,method,v);
			handleOnListener(v);
            invokeMethod(hasAfterMethodName,afterMethodName,method,v);
		}
	}

    private void invokeMethod(boolean hasMethod, String methodName, Method method,View v) {
        if (hasMethod) {
            Method[] methods = method.getDeclaringClass().getDeclaredMethods();
            for (Method m : methods) {
                if (methodName.equals(m.getName())) {
                    try {
                        m.invoke(target,v);
                    } catch (Exception e) {
                        e.printStackTrace();
                        L.e("InjectedOnClickListener before or after method[" + methodName + "] invoke is error");
                    }
                }
            }
        }
    }

}
