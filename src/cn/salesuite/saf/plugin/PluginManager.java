/**
 * 
 */
package cn.salesuite.saf.plugin;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;

/**
 * @author Tony Shen
 *
 */
public class PluginManager {

	private static PluginManager mInstance;
    private Context mContext;
    private static String mNativeLibDir;
    private static final HashMap<String, PluginClassLoader> mClassLoaderMap = new HashMap<String, PluginClassLoader>();
    private static Lock lock = new ReentrantLock();

    private PluginManager(Context context) {
        mContext = context.getApplicationContext();
        mNativeLibDir = mContext.getDir("pluginlib", Context.MODE_PRIVATE).getAbsolutePath();
    }

    public static PluginManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PluginManager.class) {
                if (mInstance == null) {
                	mInstance = new PluginManager(context);
                }
            }
        }

        return mInstance;
    }
    
    /**
     * 返回插件对应的加载器，不会重复加载同样的资源
     * @param context
     * @param pluginName
     * @param apkFilePath
     * @return
     */
    public static ClassLoader getClassLoaderByPath(Context context, String pluginName, String apkFilePath) {
    	lock.lock();
    	PluginClassLoader classloader = null;
    	try {
    		classloader = mClassLoaderMap.get(pluginName);
            if (classloader == null) {

                String dexOutputPath = context
                        .getDir(PluginUtils.PLUGIN_PATH, Context.MODE_PRIVATE).getAbsolutePath();
                
                classloader = new PluginClassLoader(apkFilePath, dexOutputPath, mNativeLibDir, context.getClassLoader());
                mClassLoaderMap.put(pluginName, classloader);
            }
    	} finally {
    		lock.lock();
    	}

        return classloader;
    }
}
