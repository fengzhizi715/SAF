/**
 * 
 */
package cn.salesuite.saf.inject;

import java.lang.reflect.Method;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * @author Tony Shen
 *
 */
public class InjectedOnTouchListener extends AbstractInjectedOnListener implements OnTouchListener{

	InjectedOnTouchListener(Object target, Method method,
			boolean invokeWithViewParam) {
		super(target, method, invokeWithViewParam);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (enabled) {
			enabled = false;
			v.post(ENABLE_AGAIN);
			handleOnListener(v);
		}
		return false;
	}

}
