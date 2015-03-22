/**
 * 
 */
package cn.salesuite.saf.plugin;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import dalvik.system.DexClassLoader;

/**
 * @author Tony Shen
 *
 */
public class PluginManager {

	private static PluginManager mInstance;
    private Context mContext;
    private String mNativeLibDir;
    private static final HashMap<String, DexClassLoader> mClassLoaderMap = new HashMap<String, DexClassLoader>();
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
    
    public static ClassLoader getClassLoaderByPath(Context c, String pluginName, String apkFilePath) throws Exception {
    	lock.lock();
    	DexClassLoader dexClassLoader = null;
    	try {
    		dexClassLoader = mClassLoaderMap.get(pluginName);
            if (dexClassLoader == null) {
                String optimizedDexOutputPath = c.getDir("odex", Context.MODE_PRIVATE).getAbsolutePath();
                dexClassLoader = new DexClassLoader(apkFilePath, optimizedDexOutputPath, null, c.getClassLoader());
                mClassLoaderMap.put(pluginName, dexClassLoader);
            }
    	} finally {
    		lock.lock();
    	}

        return dexClassLoader;
    }
}
