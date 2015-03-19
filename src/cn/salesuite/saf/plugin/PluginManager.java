/**
 * 
 */
package cn.salesuite.saf.plugin;

import java.util.HashMap;

import android.content.Context;
import dalvik.system.DexClassLoader;

/**
 * @author Tony Shen
 *
 */
public class PluginManager {
	

    private static final HashMap<String, DexClassLoader> mClassLoaderMap = new HashMap<String, DexClassLoader>();

    public static synchronized ClassLoader getClassLoaderByPath(Context c, String pluginName, String apkFilePath) throws Exception {

        DexClassLoader dexClassLoader = mClassLoaderMap.get(pluginName);
        if (dexClassLoader == null) {
            String optimizedDexOutputPath = c.getDir("odex", Context.MODE_PRIVATE).getAbsolutePath();
            dexClassLoader = new DexClassLoader(apkFilePath, optimizedDexOutputPath, null, c.getClassLoader());
            mClassLoaderMap.put(pluginName, dexClassLoader);
        }
        return dexClassLoader;
    }
}
