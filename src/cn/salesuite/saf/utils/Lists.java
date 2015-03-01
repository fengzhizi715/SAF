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
		
		if (list==null || list.size()==0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断集合是否不为空
	 * @param list
	 * @return
	 */
	public static <T> boolean isNoBlank(List<T> list) {
		
		if (list!=null && list.size()>0) {
			return true;
		} else {
			return false;
		}
	}
}
