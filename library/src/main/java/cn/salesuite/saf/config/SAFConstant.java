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
	
	/**
	 * 需要使用ImageLoader组件时,可以设置default_img_id的值,表示全局的默认图片
	 */
	public static int default_img_id;
	
	/** app存储目录/文件  可根据app的名称覆盖 默认使用saf作为文件名**/
	public static final String DIR = "/saf";
	public static final String CACHE_DIR = DIR + "/images";

	public final static String CHINA_CARRIER_UNKNOWN = "0";
	public final static String CHINA_MOBILE = "1";
	public final static String CHINA_UNICOM = "2";
	public final static String CHINA_TELECOM = "3";
	public final static String CHINA_TIETONG = "4";

	public final static String NETWORK_WIFI = "0";
	public final static String NETWORK_2G = "1";
	public final static String NETWORK_3G = "2";
	public final static String NETWORK_4G = "3";
	public final static String NO_NETWORK = "-1";
}
