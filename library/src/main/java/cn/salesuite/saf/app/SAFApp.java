/**
 * 
 */
package cn.salesuite.saf.app;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.salesuite.saf.config.SAFConstant;
import cn.salesuite.saf.rxjava.imagecache.RxImageLoader;
import cn.salesuite.saf.utils.Preconditions;
import cn.salesuite.saf.utils.SAFUtils;

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

	public RxImageLoader imageLoader;
	private static SAFApp instance;
	
	private int defaultImageId;
	private String fileDir;

	/**
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	public void init() {
		instance = this;

		activityManager = new ArrayList<Activity>();
//		if (fileDir!=null) {
//			imageLoader = new ImageLoader(instance,defaultImageId,fileDir);// 使用ImageLoader组件时,设置defaultImageId、fileDir
//		} else {
//			imageLoader = new ImageLoader(instance,defaultImageId);        // 使用ImageLoader组件时,设置defaultImageId
//		}
//
//		if (!SAFUtils.hasSdcard()) { // 当手机没有装sd卡时，图片只缓存在内存中
//			imageLoader.setEnableDiskCache(false);
//		}

		imageLoader = new RxImageLoader();
		imageLoader.init(instance);
		
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

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		imageLoader.clearMemCache();
	}
	
	@Override
	@TargetApi(14)
	public void onTrimMemory(int level) {
		if (SAFUtils.isICSOrHigher()) {
			super.onTrimMemory(level);
			
			if (level >= ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW) {
				imageLoader.clearMemCache();
			}
		}
	}
	
	/**
	 * 子类继承时,可使用自己的默认图片
	 * @param defaultImageId
	 */
	public void setDefaultImageId(int defaultImageId) {
		this.defaultImageId = defaultImageId;
	}

	/**
	 * 子类继承时,可使用自己的默认文件路径存放图片
	 * @param fileDir
	 */
	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
	}
}