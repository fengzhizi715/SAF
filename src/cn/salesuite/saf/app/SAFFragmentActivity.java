/**
 * 
 */
package cn.salesuite.saf.app;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import cn.salesuite.saf.config.SAFConstant;
import cn.salesuite.saf.net.CellIDInfo;
import cn.salesuite.saf.net.CellIDInfoManager;
import cn.salesuite.saf.utils.SAFUtil;

/**
 * @author Tony Shen
 *
 */
public class SAFFragmentActivity extends FragmentActivity{

	public static SAFApp app;
	public String TAG;
	public int networkType;
	public String networkName;
	
	private Handler mdBmHandler = new Handler(Looper.getMainLooper());
	private Runnable mGetdBmRunnable = new Runnable() {
		public void run() {
			CellIDInfoManager manager = new CellIDInfoManager();
			getSignalStrength(manager);
			if (SAFUtil.isWiFiActive(app)) {
				networkName = "wifi";
				return;
			}
			if (networkType == 0) {
				networkType = getNetworkType(manager);
			}
			networkName = SAFUtil.getNetWorkName(networkType,manager.getMnc());
			app.session.put(SAFConstant.DEVICE_NET_INFO, networkName);
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		app = (SAFApp) this.getApplication();
		if(SAFConstant.CHECK_MOBILE_STATUS) {
			checkMobileStatus();
		}

		TAG = SAFUtil.makeLogTag(this.getClass());
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
		if (SAFConstant.CHECK_MOBILE_STATUS && app.deviceid!=null) {
			mdBmHandler.removeCallbacks(mGetdBmRunnable);
		}

		delActivityFromManager(this);
	}
	
	protected void showToast(int strId) {
		Toast.makeText(this, getString(strId), Toast.LENGTH_SHORT).show();
	}

	protected void showToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 绘制中部的Toast
	 * @param strId
	 */
	protected void showMidToast(int strId) {
		Toast msg = Toast.makeText(this, getString(strId), Toast.LENGTH_SHORT);
		msg.setGravity(Gravity.CENTER, msg.getXOffset(), msg.getYOffset() / 2);
		msg.show();
	}
	
	/**
	 * 绘制中部的Toast
	 * @param str
	 */
	protected void showMidToast(String str) {
		Toast msg = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		msg.setGravity(Gravity.CENTER, msg.getXOffset(), msg.getYOffset() / 2);
		msg.show();
	}
	
	/**
	 * 检测手机状态,当手机信号弱时,利用toast提示用户
	 */
	protected void checkMobileStatus() {
		if (app.deviceid!=null) {
			mdBmHandler.post(mGetdBmRunnable);
		}
	}
	
	private void getSignalStrength(CellIDInfoManager manager) {
		int dbm = -112;
		
		ArrayList<CellIDInfo> CellID = null;
		try {
			CellID = manager.getCellIDInfo(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (CellID!=null && CellID.size()>0) {
			dbm = CellID.get(0).signal_strength;
		}

		if (dbm <= -112) {
			showToast("当前信号差");
		}
	}
	
	/**
	 * 获取手机网络类型,该方法在调用getSignalStrength()之后使用
	 * @param manager
	 * @return
	 */
	private int getNetworkType(CellIDInfoManager manager) {
		return manager.getNetworkType();
	}
}
