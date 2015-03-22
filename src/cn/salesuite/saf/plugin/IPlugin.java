/**
 * 
 */
package cn.salesuite.saf.plugin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;

/**
 * @author Tony Shen
 *
 */
public interface IPlugin {

	public void onCreate(Bundle savedInstanceState);
	
    public void onStart();
    
    public void onRestart();
    
    public void onActivityResult(int requestCode, int resultCode, Intent data);
    
    public void onResume();
    
    public void onPause();
    
    public void onStop();
    
    public void onDestroy();

	public void init(String path, Activity context,
			ClassLoader classLoader, PackageInfo packageInfo);
}
