/**
 * 
 */
package cn.salesuite.saf.utils;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 通过反射获取资源文件的帮助类<br>
 * 使用方法，可现在app的引导页面中使用如下的语句:<br>
 * <code>
 * ResourceUtil.GEN_PACKAGE_NAME = ResourceUtil.getPackageName(contex)+".R";
 * </code>
 * <br>
 * 然后就可以使用了
 * @author Tony Shen
 *
 */
public class ResourceUtil {
	
	public static String GEN_PACKAGE_NAME = "";
	
	/**
	 * 获取app的包名
	 * @param contex
	 * @return
	 */
	public static String getPackageName(Context contex) {
		PackageManager manager = contex.getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(contex.getPackageName(), 0);
			return info.packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setPackageName(String packageName) {
		StringBuilder sb = new StringBuilder();
		sb.append(packageName);
		sb.append(".R");
		GEN_PACKAGE_NAME = sb.toString();
	}
	
	/**
	 * 获取android资源id
	 * @param packageName 调用方包的名称
	 * @param resFileName 资源文件名
	 * @param parameterName
	 * @return
	 */
	public static int getResourceId(String packageName, String resFileName,
			String parameterName) {
		if ((packageName != null) && (resFileName != null)
				&& (parameterName != null))
			try {
				Class localClass = Class.forName(packageName + "$"
						+ resFileName);
				Field localField = localClass.getField(parameterName);
				Object localObject = localField.get(localClass.newInstance());
				return Integer.parseInt(localObject.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		return -1;
	}
	
	/**
	 * 获取android资源id
	 * @param resFileName 资源文件名
	 * @param parameterName
	 * @return
	 */
	public static int getResourceId(String resFileName,
			String parameterName) {
		return getResourceId(GEN_PACKAGE_NAME, resFileName,parameterName);
	}
	
	/**
	 * 获取工程中资源id
	 * @param parameterName
	 * @return
	 */
	public static int getResourceId(String parameterName) {
		return getResourceId("id",parameterName);
	}
	
	/**
	 * 获取工程中array资源id
	 * @param parameterName
	 * @return
	 */
	public static int getResourceArrayId(String parameterName) {
		return getResourceId("array",parameterName);
	}
	
	/**
	 * 获取工程中color资源id
	 * @param parameterName
	 * @return
	 */
	public static int getResourceColorId(String parameterName) {
		return getResourceId("color",parameterName);
	}
	
	/**
	 * 获取工程中drawable资源id
	 * @param parameterName
	 * @return
	 */
	public static int getResourceDrawableId(String parameterName) {
		return getResourceId("drawable",parameterName);
	}
	
	/**
	 * 获取工程中layout资源id
	 * @param parameterName
	 * @return
	 */
	public static int getResourceLayoutId(String parameterName) {
		return getResourceId("layout",parameterName);
	}
	
	/**
	 * 获取工程中string资源id
	 * @param parameterName
	 * @return
	 */
	public static int getResourceStringId(String parameterName) {
		return getResourceId("string",parameterName);
	}
}
