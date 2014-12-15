/**
 * 
 */
package cn.salesuite.saf.orm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import cn.salesuite.saf.utils.SAFUtils;

/**
 * @author Tony Shen
 *
 */
public final class DatabaseHelper extends SQLiteOpenHelper {
	
	public static final String TAG = "DatabaseHelper";
	private final static String DB_NAME = "DB_NAME";
	private final static String DB_VERSION = "DB_VERSION";
	private final static String MIGRATION_PATH = "migrations";
	
	private static boolean doInitialUpgrade = false;
	private DBVersionPrefs dbVersionPrefs;

	public DatabaseHelper(Context context) {
		super(context, getDbName(context), null, getDbVersion(context));
		doInitialUpgrade = copyAttachedDatabase(context);
		dbVersionPrefs = DBVersionPrefs.get(context);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		if (doInitialUpgrade) {
			doInitialUpgrade = false;
			onUpgrade(db, dbVersionPrefs.getDBVersion() , db.getVersion());
		} else {
			executePragmas(db);
		}
	};
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		executePragmas(db);
		executeCreate(db);
		executeMigrations(db, -1, db.getVersion());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		executePragmas(db);
		executeCreate(db);
		executeMigrations(db, oldVersion, newVersion);
	}
	
	private static String getDbName(Context context) {
		String dbname = SAFUtils.getMetaData(context, DB_NAME);

		if (dbname == null) {
			Log.i(TAG,"DB_NAME not found. Defaulting name to 'saf_db.db'.");
			dbname = "saf_db.db";
		}

		return dbname;
	}

	private static int getDbVersion(Context context) {
		Integer dbversion = SAFUtils.getMetaData(context, DB_VERSION);

		if (dbversion == null || dbversion == 0) {
			Log.i(TAG,"DB_VERSION not found. Defaulting version to 1.");
			dbversion = 1;
		}

		return dbversion;
	}
	
	public boolean copyAttachedDatabase(Context context) {
		String dbName = getDbName(context);
		final File dbPath = context.getDatabasePath(dbName);

		// If the database already exists, return
		if (dbPath.exists()) {
			return false;
		}

		// Make sure we have a path to the file
		dbPath.getParentFile().mkdirs();

		// Try to copy database file
		try {
			final InputStream inputStream = context.getAssets().open(dbName);
			final OutputStream output = new FileOutputStream(dbPath);

			byte[] buffer = new byte[1024];
			int length;

			while ((length = inputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}

			output.flush();
			output.close();
			inputStream.close();

			return true;
		}
		catch (IOException e) {
		}

		return false;
	}
	
	private void executePragmas(SQLiteDatabase db) {
		if (SQLiteUtils.FOREIGN_KEYS_SUPPORTED) {
			db.execSQL("PRAGMA foreign_keys=ON;");
			Log.i(TAG,"Foreign Keys supported. Enabling foreign key features.");
		}
	}
	
	private void executeCreate(SQLiteDatabase db) {
		db.beginTransaction();
		try {
			for (TableInfo tableInfo : DBManager.getTableInfos()) {
				String sql = SQLiteUtils.createTableDefinition(tableInfo);
				db.execSQL(sql);

				Log.i(TAG,sql);
			}
			db.setTransactionSuccessful();
		}
		finally {
			db.endTransaction();
		}
	}
	
	private boolean executeMigrations(SQLiteDatabase db, int oldVersion, int newVersion) {
		boolean migrationExecuted = false;
		try {
			final List<String> files = Arrays.asList(DBManager.getContext().getAssets().list(MIGRATION_PATH));
			Collections.sort(files, new NaturalOrderComparator());

			db.beginTransaction();
			try {
				for (String file : files) {
					try {
						final int version = Integer.valueOf(file.replace(".sql", ""));

						if (version > oldVersion && version <= newVersion) {
							executeSqlScript(db, file);
							migrationExecuted = true;

							Log.i(TAG,file + " executed succesfully.");
						}
					}
					catch (NumberFormatException e) {
					}
				}
				db.setTransactionSuccessful();
			}
			finally {
				db.endTransaction();
			}
		}
		catch (IOException e) {
		}

		dbVersionPrefs.setDBVersion(db.getVersion());
		dbVersionPrefs.save();
		
		return migrationExecuted;
	}
	
	private void executeSqlScript(SQLiteDatabase db, String file) {
		try {
			final InputStream input = DBManager.getContext().getAssets().open(MIGRATION_PATH + "/" + file);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = null;

			while ((line = reader.readLine()) != null) {
				db.execSQL(line.replace(";", ""));
			}
		}
		catch (IOException e) {
		}
	}

}
