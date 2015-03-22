/**
 * 
 */
package cn.salesuite.saf.plugin;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;

/**
 * @author Tony Shen
 *
 */
public class PluginContext extends ContextThemeWrapper {
	
	private AssetManager mAsset;
	private Resources mResources;
	private Theme mTheme;
	private int mThemeResId;
	private ClassLoader mClassLoader;
	private Context mContext;

	public PluginContext(Context context, int themeres, String apkPath, ClassLoader classLoader) {
		super(context, themeres);
		mContext = context;
        mAsset = getPluginAssets(apkPath);
        mResources = getPluginResources(context, mAsset);
		mTheme = getPluginTheme(mResources);
		mClassLoader = classLoader;
	}
	
	private AssetManager getPluginAssets(String apkPath) {
		AssetManager instance = null;
		try {
			instance = AssetManager.class.newInstance();
			Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
			addAssetPathMethod.invoke(instance, apkPath);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return instance;
	}

	private Resources getPluginResources(Context ctx, AssetManager asset) {
		DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
		Configuration con = ctx.getResources().getConfiguration();
		return new Resources(asset, metrics, con);
	}

	private Theme getPluginTheme(Resources resources) {
		Theme theme = resources.newTheme();
		mThemeResId = getInnerRIdValue("com.android.internal.R.style.Theme");
		theme.applyStyle(mThemeResId, true);
		return theme;
	}
	
	private int getInnerRIdValue(String RStrnig) {
		int value = -1;
		try {
			int rindex = RStrnig.indexOf(".R.");
			String Rpath = RStrnig.substring(0, rindex + 2);
			int fieldIndex = RStrnig.lastIndexOf(".");
			String fieldName = RStrnig.substring(fieldIndex + 1, RStrnig.length());
			RStrnig = RStrnig.substring(0, fieldIndex);
			String type = RStrnig.substring(RStrnig.lastIndexOf(".") + 1, RStrnig.length());
			String className = Rpath + "$" + type;

			Class<?> cls = Class.forName(className);
			value = cls.getDeclaredField(fieldName).getInt(null);

		} catch (Throwable e) {
			e.printStackTrace();
		}
		return value;
	}

	public AssetManager getAssets() {
		return mAsset;
	}

	public Resources getResources() {
		return mResources;
	}

	public Theme getTheme() {
		return mTheme;
	}
	
}
