/**
 * 
 */
package cn.salesuite.saf.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.salesuite.saf.app.SAFApp;
import cn.salesuite.saf.reflect.Reflect;

/**
 * SAF的工具类
 * @author Tony Shen
 *
 */
public class SAFUtils {

//	private static final int REQUEST_EXTERNAL_STORAGE = 1;
//	private static String[] PERMISSIONS_STORAGE = {
//			Manifest.permission.READ_EXTERNAL_STORAGE,
//			Manifest.permission.WRITE_EXTERNAL_STORAGE
//	};
	
	public static boolean isFroyoOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }
	
	public static boolean isGingerbreadOrHigher() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}
	
	public static boolean isHoneycombOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
	
	public static boolean isICSOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }
	
	public static boolean isJellyBeanOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }
	
	@TargetApi(17)
	public static boolean isJellyBeanMR1OrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
	
	@TargetApi(18)
	public static boolean isJellyBeanMR2OrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
	
	@TargetApi(19)
	public static boolean isKitkatOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
	
	@TargetApi(20)
	public static boolean isKitkatWatchOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH;
    }
	
	@TargetApi(21)
	public static boolean isLOrHigher() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
	}

	/**
	 * api level 22是android 5.1
	 * @return
     */
	@TargetApi(22)
	public static boolean isLMR1OrHigher() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
	}

	@TargetApi(23)
	public static boolean isMOrHigher() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	public static boolean isWiFiActive(Context context) { 
		WifiManager wm=null;
		try{
			wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(wm==null || wm.isWifiEnabled()==false) return false;
		
		return true;
    }
	
	/**
	 * 安装apk
	 * @param fileName apk文件的绝对路径
	 * @param context
	 */
	public static void installAPK(String fileName, Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	/**
	 * 判断某个应用当前是否正在运行
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppRunning(Context context, String packageName) {
		if (packageName == null)
			return false;

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断服务是否运行
	 * @param mContext
	 * @param serviceName
	 * @return true为运行，false为不在运行
	 */
	public boolean isServiceRunning(Context mContext, String serviceName) {
		ActivityManager myManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			String serName = runningService.get(i).service.getClassName().toString();
			if (serName.equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断app是否处于前台,只使用了android 5.0以后
	 * 通过使用UsageStatsManager获取，此方法是android5.0A之后提供的API
	 * 必须：
	 * 1. 此方法只在android5.0以上有效
	 * 2. AndroidManifest中加入此权限<uses-permission xmlns:tools="http://schemas.android.com/tools" android:name="android.permission.PACKAGE_USAGE_STATS"
	 * tools:ignore="ProtectedPermissions" />
	 * 3. 打开手机设置，点击安全-高级，在有权查看使用情况的应用中，为这个App打上勾
	 *
	 * @param context     上下文参数
	 * @param packageName 需要检查是否位于栈顶的App的包名
	 * @return
	 */
	@TargetApi(21)
	public static boolean queryUsageStats(Context context, String packageName) {
		class RecentUseComparator implements Comparator<UsageStats> {
			@Override
			public int compare(UsageStats lhs, UsageStats rhs) {
				return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
			}
		}
		RecentUseComparator mRecentComp = new RecentUseComparator();
		long ts = System.currentTimeMillis();
		UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");
		List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 1000 * 10, ts);
		if (Preconditions.isBlank(usageStats)) {
			if (!havaPermissionForTest(context)) {
				Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				Toast.makeText(context, "权限不够\n请打开手机设置，点击安全-高级，在有权查看使用情况的应用中，为这个App打上勾", Toast.LENGTH_SHORT).show();
			}
			return false;
		}
		Collections.sort(usageStats, mRecentComp);
		String currentTopPackage = usageStats.get(0).getPackageName();
		return currentTopPackage.equals(packageName);
	}

	/**
	 * 判断是否有用权限
	 *
	 * @param context 上下文参数
	 */
	@TargetApi(19)
	private static boolean havaPermissionForTest(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
			AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
			int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
			return (mode == AppOpsManager.MODE_ALLOWED);
		} catch (PackageManager.NameNotFoundException e) {
			return true;
		}
	}
	
	/**
	 * 获取手机网络类型名称
	 * @param networkType
	 * @param mnc Mobile NetworkCode，移动网络码，共2位
	 * @return
	 */
	public static String getNetWorkName(int networkType,String mnc) {
		if (networkType == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
			return "Network type is unknown";
		} else if (networkType == TelephonyManager.NETWORK_TYPE_CDMA) {
			return "电信2G";
		} else if (networkType == TelephonyManager.NETWORK_TYPE_EVDO_0) {
			return "电信3G";
		} else if (networkType == TelephonyManager.NETWORK_TYPE_GPRS || networkType == TelephonyManager.NETWORK_TYPE_EDGE) {
			if ("00".equals(mnc) || "02".equals(mnc)) {
				return "移动2G";
			} else if ("01".equals(mnc)) {
				return "联通2G";
			}
		} else if (networkType == TelephonyManager.NETWORK_TYPE_UMTS || networkType == TelephonyManager.NETWORK_TYPE_HSDPA) {
			return "联通3G";
		}
		return null;
	}
	
	/**
	 * 检测网络状态
	 * @param context
	 * @return
	 */
	public static boolean checkNetworkStatus(Context context){
		boolean resp = false;
		final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connMgr.getActiveNetworkInfo();   
		if (activeNetInfo != null && activeNetInfo.isAvailable()) {
			resp = true;
		}
		return resp;
	}
	
	/**
	 * 检测gps状态
	 * @param context
	 * @return
	 */
	public static boolean checkGPSStatus(Context context){
		boolean resp = false;
		LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);  
        if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
        	resp = true;            
        }  
        return resp;
	}
	
	/**
	 * 生成app日志tag
	 * @param cls
	 * @return
	 */
	public static String makeLogTag(Class<?> cls) {
		return cls.getSimpleName();
	}
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
	  final float scale = context.getResources().getDisplayMetrics().density;
	  return (int) (dpValue * scale + 0.5f);
	}
	
	/**
	 * 根据手机的分辨率从  px(像素) 转成为dp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
	    return (int)(0.5F + pxValue / scale);
	}
	
	/**
	 * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
	 * @param context
	 * @param spValue
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (spValue * scale + 0.5f);
	}
	
	/**
	 * 根据手机的分辨率从  px(像素) 转成为sp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
    /**
     * 判断谷歌地图是否可用,某些国行的手机不支持谷歌地图的服务
     * @return
     */
	public static boolean googleMapAvailable() {
		boolean available = false;
		try{
			Class.forName("com.google.android.maps.MapActivity");
			available = true;
		} catch (Exception e)  {
		}
		return available;
	}
	
	/**
	 * 从Assets中读取文件
	 * @param context
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getFromAssets(Context context,String fileName)
			throws FileNotFoundException {
		InputStream inputStream = null;
		try {
			inputStream = context.getResources().getAssets().open(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputStream;
	}
	
	/**
	 * 将对象以json格式打印出来
	 * @param obj
	 * @return
	 */
	public static String printObject(Object obj) {
		return obj!=null?JSON.toJSONString(obj):"{}";
	}

	/**
	 * 对文件设置root权限
	 * @param filePath
	 * @return
	 */
	public static boolean upgradeRootPermission(String filePath) {
	    Process process = null;
	    DataOutputStream os = null;
	    try {
	        String cmd="chmod 777 " + filePath;
	        process = Runtime.getRuntime().exec("su"); //切换到root帐号
	        os = new DataOutputStream(process.getOutputStream());
	        os.writeBytes(cmd + "\n");
	        os.writeBytes("exit\n");
	        os.flush();
	        process.waitFor();
	    } catch (Exception e) {
	        return false;
	    } finally {
	        try {
	            if (os != null) {
	                os.close();
	            }
	            process.destroy();
	        } catch (Exception e) {
	        }
	    }
	    return true;
	}
	
	/**
	 * 判断当前线程是否为ui线程
	 * @return
	 */
	public static boolean isUIThread(){
    	long uiId = Looper.getMainLooper().getThread().getId();
    	long cId = Thread.currentThread().getId();
    	return uiId == cId;	
    }

	/**
	 * 关闭虚拟键盘
	 * @param context
	 * @param view
	 */
	public static void hideSoftInputFromWindow(Context context,View view) {
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);  
	}
	
	/**
	 * 关闭虚拟键盘
	 * @param context
	 * @param views
	 */
	public static void hideSoftInputFromWindow(Context context,View... views) {
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (views!=null && views.length>0) {
			for (View view:views) {
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		}
	}
	
	/**
	 * 获取AndroidManifest.xml中<meta-data>元素的值
	 * @param context
	 * @param name
	 * @return
	 */
	public static <T> T getMetaData(Context context, String name) {
		try {
			final ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);

			if (ai.metaData != null) {
				return (T) ai.metaData.get(name);
			}
		}
		catch (Exception e) {
			System.out.print("Couldn't find meta-data: " + name);
		}

		return null;
	}
	
	/**
	 * 判断经纬度是否在中国
	 * @param mLocation
	 * @return
	 */
	public static boolean positionInChina(Location mLocation){
		if(mLocation.getLatitude()>18.167 && mLocation.getLatitude()<53.55){
			if(mLocation.getLongitude()>73.667 && mLocation.getLongitude()<135.033){
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断经纬度是否在中国
	 * @param latitude
	 * @param longitude
     * @return
     */
	public static boolean positionInChina(double latitude,double longitude){
		if(latitude>18.167 && latitude<53.55){
			if(longitude>73.667 && longitude<135.033){
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否存在sd卡
	 * @return
	 */
	public static boolean hasSdcard() {
		
		String status = Environment.getExternalStorageState();
		
		return status.equals(Environment.MEDIA_MOUNTED)?true:false;
	}
	
	/**
	 * 获取手机可用的内存空间 
	 * 返回单位 G  如  “1.5GB”
	 * 
	 * @return
	 */
	public static String getAvailableSDRomString() {
		if (!hasSdcard())
			return "0";
		
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return Formatter.formatFileSize(SAFApp.getInstance().getApplicationContext(), availableBlocks
				* blockSize);
	}
	
	/**
	 * 获取SD 卡内存
	 * @return
	 */
	public static long getAvailableSD() {
		if (!hasSdcard())
			return 0;
		
		String storageDirectory = Environment.getExternalStorageDirectory().toString();

		try {
			StatFs stat = new StatFs(storageDirectory);
			long avaliableSize = ((long) stat.getAvailableBlocks() * (long) stat
					.getBlockSize());
			return avaliableSize;
		} catch (RuntimeException ex) {
			return 0;
		}
	}
	
	/**
	 * 获取手机可用的cpu数
	 * @return
	 */
	public static int getAvailableProcessors() {
		return Runtime.getRuntime().availableProcessors();
	}

	/**
	 * 获取全局的context
	 * @return
     */
	@TargetApi(14)
	public static Context getContext() {
		return Reflect.on("android.app.ActivityThread").call("currentApplication").get();
	}

	@TargetApi(23)
	public static boolean verifyStoragePermissions(Context context) {
		// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

//		if (permission != PackageManager.PERMISSION_GRANTED) {
//			// We don't have permission so prompt the user
//			ActivityCompat.requestPermissions(
//					activity,
//					PERMISSIONS_STORAGE,
//					REQUEST_EXTERNAL_STORAGE
//			);
//		}

		return permission == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * 检查权限是否开启
	 *
	 * @param permission
	 * @return true or false
	 */
	public static boolean checkPermissions(Context context, String permission) {

		if (context==null) {
			if (SAFUtils.isICSOrHigher()) {
				context = getContext();
			} else {
				return false;
			}
		}

		PackageManager localPackageManager = context.getApplicationContext().getPackageManager();
		return localPackageManager.checkPermission(permission, context.getApplicationContext().getPackageName()) == PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * 获取当前进程的名称
	 * @param context
	 * @return
     */
	public static String getProcessName(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();

		if (Preconditions.isNotBlank(runningApps)) {
			int myPid = android.os.Process.myPid();
			for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
				if (proInfo!=null && proInfo.pid == myPid) {
					return proInfo.processName;
				}
			}
		}

		return null;
	}
}
