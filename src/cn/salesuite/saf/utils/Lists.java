/**
 * 
 */
package cn.salesuite.saf.utils;

import java.util.List;

/**
 * @author Tony Shen
 *
 */
public class Lists {
	
	/**
	 * 判断集合是否为空
	 * @param list
	 * @return
	 */
	public static <T> boolean isBlank(List<T> list) {
		
		return list==null || list.size()==0;
	}

	/**
	 * 判断集合是否不为空
	 * @param list
	 * @return
	 */
	public static <T> boolean isNoBlank(List<T> list) {
		
		return list!=null && list.size()>0;
	}
}
