/**
 * 
 */
package cn.salesuite.saf.plugin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import cn.salesuite.saf.app.SAFActivity;

/**
 * 插件中的类必须继承BasePluginActivity
 * @author Tony Shen
 *
 */
public class BasePluginActivity extends SAFActivity implements IPlugin{

    private boolean isFromPlugin;
    private ClassLoader mDexClassLoader;
    private String mApkFilePath;
    private PackageInfo mPackageInfo;
    private PluginContext mContext;
    private PluginManager mPluginManager;
    private Activity proxyActivity;
    private Activity mActivity;
    
	@Override
	public void init(String path, Activity context,
			ClassLoader classLoader, PackageInfo packageInfo) {
		isFromPlugin = true;
        mDexClassLoader = classLoader;
        mApkFilePath = path;
        mPackageInfo = packageInfo;
        mActivity = context;
        mContext = new PluginContext(context, 0, mApkFilePath, mDexClassLoader);
        mPluginManager = PluginManager.getInstance(mContext);
        
        attachBaseContext(mContext);
	}
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
        if (isFromPlugin) {
        	proxyActivity = mActivity;
        } else {
            super.onCreate(savedInstanceState);
            proxyActivity = this;
        }
	}

	@Override
	public void onStart() {
        if (isFromPlugin) {
            return;
        }
		super.onStart();
	}

	@Override
	public void onRestart() {
        if (isFromPlugin) {
            return;
        }
		super.onRestart();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
        if (isFromPlugin) {
            return;
        }
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
