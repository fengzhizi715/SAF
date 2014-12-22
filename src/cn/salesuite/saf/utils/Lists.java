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

	public static <T> boolean isNoBlank(List<T> list) {
		
		if (list!=null && list.size()>0) {
			return true;
		} else {
			return false;
		}
	}
}
