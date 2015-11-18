/**
 * 
 */
package cn.salesuite.saf.plugin;

import dalvik.system.DexClassLoader;

/**
 * 每一个插件对应一个classloader
 * @author Tony Shen
 *
 */
public class PluginClassLoader extends DexClassLoader {

    private String apkPath;

	public PluginClassLoader(String dexPath, String optimizedDirectory,
			String libraryPath, ClassLoader parent) {
		super(dexPath, optimizedDirectory, libraryPath, parent);
		apkPath = dexPath;
		NativeLibManager.unPackSOFromApk(dexPath, libraryPath);
	}
    
	public String getApkPath() {
		return apkPath;
	}
}
