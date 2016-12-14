/**
 * 
 */
package cn.salesuite.router;

import android.app.Activity;

import java.util.Map;

/**
 * @author Tony Shen
 *
 */
public class RouterParameter {

	public RouterOptions routerOptions;
	public Map<String, String> openParams;

	public static class RouterOptions {
		public Class<? extends Activity> clazz; // 跳转到的class
		public int enterAnim;                   // activity进入的动画
		public int exitAnim;                    // activity离开的动画
	}
}
