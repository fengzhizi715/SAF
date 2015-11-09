/**
 * 
 */
package cn.salesuite.saf.plugin;

import java.io.File;

import android.content.Context;

/**
 * @author Tony Shen
 *
 */
public class PluginUtils {

	public static final String PLUGIN_PATH = "plugins";
    
    public static File getInstallPath(Context context, String pluginName) {
        File pluginDir = getPluginPath(context);
        if (pluginDir == null) {
            return null;
        }
        
        int suffix = pluginName.lastIndexOf('.');
        if (suffix > -1 && !pluginName.substring(suffix).equalsIgnoreCase(".apk")) {
            pluginName = pluginName.substring(0, suffix) + ".apk";
        } else if (suffix == -1) {
            pluginName = pluginName + ".apk";
        }
        return new File(pluginDir, pluginName);
    }
    
    public static File getPluginPath(Context context) {
        return context.getDir(PLUGIN_PATH, Context.MODE_PRIVATE);
    }
}
