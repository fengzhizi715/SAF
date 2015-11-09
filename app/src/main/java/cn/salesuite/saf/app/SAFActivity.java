/**
 * 
 */
package cn.salesuite.saf.app;

import java.lang.ref.WeakReference;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.salesuite.saf.utils.SAFUtils;
import cn.salesuite.saf.utils.ToastUtils;

/**
 * SAF框架基类的Activity
 * @author Tony Shen
 *
 */
public class SAFActivity extends Activity{

	public static SAFApp app;
	public String TAG;
	public int networkType;
	public String networkName;
    protected Handler mHandler = new SafeHandler(this);
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		app = (SAFApp) this.getApplication();
		TAG = SAFUtils.makeLogTag(this.getClass());
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
	public void onLowMemory() {
		super.onLowMemory();
		app.imageLoader.clearMemCache();
	}
	
	@Override
	@TargetApi(14)
	public void onTrimMemory(int level) {
		if (SAFUtils.isICSOrHigher()) {
			super.onTrimMemory(level);
			
			if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
				app.imageLoader.clearCache();
			}
		}
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
	
	/**
	 * 防止内部Handler类引起内存泄露
	 * @author Tony Shen
	 *
	 */
    public static class SafeHandler extends Handler{
	    private final WeakReference<Activity> mActivity;
	    public SafeHandler(Activity activity) {
	        mActivity = new WeakReference<Activity>(activity);
	    }
	    @Override
	    public void handleMessage(Message msg) {
	        if(mActivity.get() == null) {
	            return;
	        }
	    }
	}
}
