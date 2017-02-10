/**
 * 
 */
package cn.salesuite.saf.orm;

import java.util.Collection;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 如果要使用sqlite orm，必须在application中先使用DBManager.initialize(this);<br>
 * 如果结合SAFApp的功能，可以先写一个application继承SAFApp，然后将DBManager.initialize(this);
 * @author Tony Shen
 *
 */
public final class DBManager {
	
	public static final int DEFAULT_CACHE_SIZE = 1024;
	public static final String TAG = "DBManager";
	
	private static boolean isInitialized = false;
	private static Context mContext;
	private static DomainInfo mDomainInfo;
	private static DatabaseHelper mDatabaseHelper;
	private static LruCache<String, DBDomain> mEntities;
	
	private DBManager() {
	}
	
	public static void initialize(Application app) {
		initialize(app,DEFAULT_CACHE_SIZE);
	}

	public static void initialize(Application app, int cacheSize) {
		if (isInitialized) {
			Log.i(TAG,"DBManager already initialized.");
			return;
		}
		
		mContext = app;
		mDomainInfo = new DomainInfo(app);
		mDatabaseHelper = new DatabaseHelper(app);
		mEntities = new LruCache<String,DBDomain>(cacheSize);
		openDatabase();
		isInitialized = true;

		Log.i(TAG,"DBManager initialized successfully.");
	}
	
	public static synchronized void close() {
		if (mDatabaseHelper!=null) {
			mDatabaseHelper.close();
		}
		mDatabaseHelper = null;
		
		if (mEntities!=null) {
			mEntities.evictAll();
		}
		mEntities = null;
		
		mDomainInfo = null;
		isInitialized = false;

		Log.i(TAG,"DBManager closed.");
	}

	public static Context getContext() {
		return mContext;
	}

	public static synchronized SQLiteDatabase openDatabase() {
		return mDatabaseHelper.getWritableDatabase();
	}

	public static synchronized Collection<TableInfo> getTableInfos() {
		return mDomainInfo.getTableInfos();
	}
	
	public static synchronized TableInfo getTableInfo(Class<? extends DBDomain> type) {
		return mDomainInfo.getTableInfo(type);
	}
	
	public static synchronized String getTableName(Class<? extends DBDomain> type) {
		return mDomainInfo.getTableInfo(type).getTableName();
	}
	
	public static String getId(Class<? extends DBDomain> type, Long id) {
		return getTableName(type) + "@" + id;
	}

	public static String getId(DBDomain entity) {
		return getId(entity.getClass(), entity.getId());
	}

	public static synchronized void addEntity(DBDomain entity) {
		mEntities.put(getId(entity), entity);
	}
	
	public static synchronized DBDomain getEntity(Class<? extends DBDomain> type, long id) {
		return mEntities.get(getId(type, id));
	}
}
