/**
 * 
 */
package cn.salesuite.saf.config;

/**
 * @author Tony Shen
 *
 */
public class SAFConstant {

	//os类型
	public static final String SPECIAL_IMEI="000000000000000";
	public static final String SPECIAL_ANDROID_ID="9774d56d682e549c";
	
	public static final String SHARED = "SAF";
	
	/**
	 * 该变量设置成true时，可读取手机信号强度和手机卡类型,并且需要添加权限&ltuses-permission android:name="android.permission.READ_PHONE_STATE" />
	 * 默认情况该值为false
	 */
	public static boolean CHECK_MOBILE_STATUS = false;
	
	public static String DEVICE_NET_INFO = "devicenetinfo";
}
