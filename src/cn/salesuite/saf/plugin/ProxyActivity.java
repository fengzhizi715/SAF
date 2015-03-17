/**
 * 
 */
package cn.salesuite.saf.plugin;

import java.io.File;

import android.os.Bundle;
import cn.salesuite.saf.app.SAFActivity;
import cn.salesuite.saf.inject.Injector;
import cn.salesuite.saf.inject.annotation.InjectExtra;
import cn.salesuite.saf.utils.StringUtils;

/**
 * @author Tony Shen
 *
 */
public class ProxyActivity extends SAFActivity {
	
	// 约定大于配置，需要传"plugin_name"、"activity_name"
	@InjectExtra(key=PLUGIN_NAME)
	String mPluginName;
	
	@InjectExtra(key=ACTIVITY_NAME)
	String mActivityName;

	IPlugin mPluginActivity;
	
	private static final String PLUGIN_NAME = "plugin_name";
	private static final String ACTIVITY_NAME = "activity_name";
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.injectInto(this);
        
        if (!StringUtils.isNotBlank(mPluginName,mActivityName)) {
        	return;
        }
        
        File pluginFile = PluginUtils.getInstallPath(this, mPluginName);
        
        if(!pluginFile.exists()){
            return;
        }
        
        
    }
	
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
