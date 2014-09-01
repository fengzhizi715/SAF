/**
 * 
 */
package cn.salesuite.saf.inject;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author Tony Shen
 *
 */
public class InjectedOnClickListener implements OnClickListener {

    private final Object target;
    private final Method method;
    private final boolean invokeWithViewParam;
	private static boolean enabled = true;

	private static final Runnable ENABLE_AGAIN = new Runnable() {
		@Override
		public void run() {
			enabled = true;
		}
	};

	InjectedOnClickListener(Object target, Method method,boolean invokeWithViewParam) {
		this.target = target;
		this.method = method;
		this.invokeWithViewParam = invokeWithViewParam;
	}
	  
	@Override
	public final void onClick(View v) {
		if (enabled) {
			enabled = false;
			v.post(ENABLE_AGAIN);
			handleOnClick(v);
		}
	}

	private void handleOnClick(View view) {
        try {
            if (invokeWithViewParam) {
                method.invoke(target, view);
            } else {
                method.invoke(target);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
