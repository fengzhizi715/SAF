/**
 * 
 */
package cn.salesuite.saf.plugin;

import dalvik.system.DexClassLoader;

/**
 * @author Tony Shen
 *
 */
public class PluginClassLoader extends DexClassLoader {

	public PluginClassLoader(String dexPath, String optimizedDirectory,
			String libraryPath, ClassLoader parent) {
		super(dexPath, optimizedDirectory, libraryPath, parent);
	}

}
