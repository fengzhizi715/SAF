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
	
	/**
	 * 需要使用ImageLoader组件时,可以设置default_img_id的值,表示全局的默认图片
	 */
	public static int default_img_id;
	
	/** app存储目录/文件  可根据app的名称覆盖 默认使用saf作为文件名**/
	public static String DIR = "/saf";
	public static String CACHE_DIR = DIR + "/images";
}
