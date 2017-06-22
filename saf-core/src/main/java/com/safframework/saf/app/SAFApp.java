/**
 * 
 */
package com.safframework.saf.app;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.safframework.saf.config.SAFConstant;
import com.safframework.saf.utils.SAFUtils;
import com.safframework.tony.common.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * SAFApp是自定义的Application,session可作为缓存存放app的全局变量<br>
 * SAFApp并不是每个app都需要使用,可自由选择,也可以继承它<br>
 * 如需使用SAFApp,则在AndroidManifest.xml中配置,<br>
 * 在application中增加android:name="cn.salesuite.saf.app.SAFApp"
 * 
 * @author Tony Shen
 * 
 */
public class SAFApp extends Application {

	public String root = "/sdcard";

	public List<Activity> activityManager;

	public String deviceid;  // 设备ID
	public String osVersion; // 操作系统版本
	public String mobileType;// 手机型号
	public String version;   // app的versionName
	public int versionCode;  // app的versionCode

	private static SAFApp instance;

	/**
	 * @see Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	public void init() {
		instance = this;

		activityManager = new ArrayList<Activity>();
		
		PackageManager manager = this.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			deviceid = getDeviceId();
			osVersion = Build.VERSION.RELEASE;
			mobileType = Build.MODEL;
			if (null != info) {
				version = info.versionName;
				versionCode = info.versionCode;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取手机的设备号（imei）
	 * 
	 * @return
	 */
	@TargetApi(23)
	private String getDeviceId() {
		String  imei = null;

		if (SAFUtils.isMOrHigher()) {
			if (getPackageManager().checkPermission(Manifest.permission.READ_PHONE_STATE,
					getPackageName()) == PackageManager.PERMISSION_GRANTED) {
				TelephonyManager mphonemanger = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE));
				if (mphonemanger !=null) {
					imei = mphonemanger.getDeviceId();
				}
			} else {
				Log.e("SAFApp","no android.permission.READ_PHONE_STATE permission");
			}
		} else {
			TelephonyManager mphonemanger = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			if (mphonemanger != null) {
				imei = mphonemanger.getDeviceId();
			}
		}

		if (Preconditions.isBlank(imei)) {
			imei = SAFConstant.SPECIAL_IMEI;
		}

		return imei;
	}

	public static SAFApp getInstance() {
		return instance;
	}
}