/**
 * 
 */
package cn.salesuite.saf.orm;

import android.content.Context;
import cn.salesuite.saf.prefs.BasePrefs;

/**
 * @author Tony Shen
 *
 */
public class DBVersionPrefs extends BasePrefs{

	private static final String PREFS_NAME = "DBVersionPrefs";
	public static final String DB_VERSION = "db_version";
	
	private DBVersionPrefs(Context context) {
		super(context, PREFS_NAME);
	}

	public static DBVersionPrefs get(Context context) {
		return new DBVersionPrefs(context);
	}
	
	public int getDBVersion() {
		return getInt(DB_VERSION, -1);
	}
	
	public void setDBVersion(int v) {
		putInt(DB_VERSION, v);
	}
}
