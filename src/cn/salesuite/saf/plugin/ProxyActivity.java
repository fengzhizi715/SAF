/**
 * 
 */
package cn.salesuite.saf.plugin;

import cn.salesuite.saf.app.SAFActivity;

/**
 * @author Tony Shen
 *
 */
public class ProxyActivity extends SAFActivity {

	IPlugin mPluginActivity;
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mPluginActivity != null) {
			mPluginActivity.onResume();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mPluginActivity != null) {
			mPluginActivity.onStart();
		}
	}
}
