package cn.salesuite.saf.inject;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnLongClickListener;

/**
 * @author frankswu
 *
 */
public class InjectedOnLongClickListener extends AbstractInjectedOnListener implements OnLongClickListener{

	InjectedOnLongClickListener(Object target, Method method,
			boolean invokeWithViewParam) {
		super(target, method, invokeWithViewParam);
	}

	@Override
	public boolean onLongClick(View view) {
		if (enabled) {
			enabled = false;
			view.post(ENABLE_AGAIN);
			handleOnListener(view);
			return true;
		}
		return false;
	}

	
	
}
