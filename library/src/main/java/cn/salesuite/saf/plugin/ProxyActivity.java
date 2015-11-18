/**
 * 
 */
package cn.salesuite.saf.plugin;

import java.io.File;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

	private IPlugin mPluginActivity;
	
	private String mPluginApkFilePath;
	
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
        
        mPluginApkFilePath = pluginFile.getAbsolutePath();

        try {
            initPlugin();
            mPluginActivity.onCreate(savedInstanceState);
        } catch (Exception e) {
            mPluginActivity = null;
            e.printStackTrace();
        }
    }
	
	private void initPlugin() {
		
		PackageInfo packageInfo;
		try {
			PackageManager pm = getPackageManager();
			packageInfo = pm.getPackageArchiveInfo(mPluginApkFilePath,
					PackageManager.GET_ACTIVITIES);

			ClassLoader classLoader = PluginManager.getClassLoaderByPath(this,
					mPluginName, mPluginApkFilePath);
			Class<?> mClassLaunchActivity = (Class<?>) classLoader
					.loadClass(mActivityName);

			getIntent().setExtrasClassLoader(classLoader);
			mPluginActivity = (IPlugin) mClassLaunchActivity.newInstance();
	        mPluginActivity.init(mPluginApkFilePath, this, classLoader, packageInfo);
		} catch (Exception e) {
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
	
    @Override
    protected void onRestart() {
        super.onRestart();
        if(mPluginActivity != null) {
            mPluginActivity.onRestart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mPluginActivity != null) {
            mPluginActivity.onStop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mPluginActivity != null) {
            mPluginActivity.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPluginActivity != null) {
            mPluginActivity.onDestroy();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (mPluginActivity!=null) {
        	mPluginActivity.onActivityResult(requestCode, resultCode, data);
    	}
        super.onActivityResult(requestCode, resultCode, data);
    }
}
