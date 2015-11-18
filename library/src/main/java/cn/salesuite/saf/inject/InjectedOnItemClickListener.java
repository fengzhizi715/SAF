package cn.salesuite.saf.inject;

import java.lang.reflect.Method;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author frankswu
 *
 */
public class InjectedOnItemClickListener extends AbstractInjectedOnListener
		implements OnItemClickListener {

	InjectedOnItemClickListener(Object target, Method method,
			boolean invokeWithViewParam) {
		super(target, method, invokeWithViewParam);
	}

    InjectedOnItemClickListener(Object target, Method method,boolean invokeWithViewParam, String beforeMethodName, String afterMethodName) {
        super(target, method, invokeWithViewParam,beforeMethodName, afterMethodName);
    }
	@Override
	public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
		if (enabled) {
			enabled = false;
			view.post(ENABLE_AGAIN);
            invokeMethod(hasBeforeMethodName,beforeMethodName,method,view,position,id);
            handleOnListener(parentView,view,position,id);
            invokeMethod(hasAfterMethodName,afterMethodName,method,view,position,id);
		}

	}
}
