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
}
