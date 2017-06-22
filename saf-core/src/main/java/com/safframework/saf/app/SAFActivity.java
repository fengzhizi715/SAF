/**
 * 
 */
package com.safframework.saf.app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.safframework.log.L;
import com.safframework.saf.utils.SAFUtils;
import com.safframework.saf.utils.ToastUtils;


/**
 * SAF框架基类的Activity
 * @author Tony Shen
 *
 */
public class SAFActivity extends Activity{

	public SAFApp app;
	public String TAG;
	protected Context mContext;
    protected Handler mHandler = new SafeHandler(this);
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		app = (SAFApp) this.getApplication();

		mContext = this;

		TAG = SAFUtils.makeLogTag(this.getClass());
		L.init(TAG);
		addActivityToManager(this);
	}
	
	protected  void addActivityToManager(Activity act) {
    	Log.i(TAG, "addActivityToManager");
        if (!app.activityManager.contains(act)) {
        	 Log.i(TAG , "addActivityToManager, packagename = " + act.getClass().getName()) ;
        	 app.activityManager.add(act);
	    }
	}
	
	protected void closeAllActivities() {
		Log.i(TAG, "closeAllActivities");
		for (final Activity act : app.activityManager) {
			if (act != null) {
				act.finish();
			}
		}
	}
	
	protected  void delActivityFromManager(Activity act) {
    	Log.i(TAG,"delActivityFromManager") ;
        if (app.activityManager.contains(act)) {
        	app.activityManager.remove(act);
        }
	}
	
	/**
	 * 返回当前运行activity的名称
	 * @return
	 */
	protected String getCurrentActivityName() {
		int size = app.activityManager.size();
		if (size > 0) {
			return app.activityManager.get(size-1).getClass().getName();
		}
		return null;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		delActivityFromManager(this);
	}
	
	/**
	 * @param message toast的内容
	 */
	protected void toast(String message) {
		ToastUtils.showShort(this, message);
	}

	/**
	 * @param resId toast的内容来自String.xml
	 */
	protected void toast(int resId) {
		ToastUtils.showShort(this, resId);
	}
}
