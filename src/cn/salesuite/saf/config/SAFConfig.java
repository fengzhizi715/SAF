/**
 * 
 */
package cn.salesuite.saf.config;

/**
 * @author Tony Shen
 *
 */
public class SAFConfig {
	
	/**
	 * Set tag name
	 * 0:Simple Name
	 * 1:Canonical Name
	 */
	public static int TAG_LEVEL = 0;
	
	/**
	 * 需要使用ImageLoader组件时,可以设置default_img_id的值,表示全局的默认图片
	 */
	public static int default_img_id;
	
	/** app存储目录/文件  可根据app的名称覆盖 默认使用saf作为文件名**/
	public static String DIR = "/saf";
	public static String CACHE_DIR = DIR + "/images";
}
