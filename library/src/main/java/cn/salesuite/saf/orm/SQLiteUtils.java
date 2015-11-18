/**
 * 
 */
package cn.salesuite.saf.orm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import cn.salesuite.saf.orm.annotation.Column;

/**
 * @author Tony Shen
 *
 */
public class SQLiteUtils {

	public static final String TAG = "SQLiteUtils";
	public static final boolean FOREIGN_KEYS_SUPPORTED = Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	
	public enum SQLiteType {
		INTEGER, REAL, TEXT, BLOB
	}
	
	@SuppressWarnings("serial")
	private static final HashMap<Class<?>, SQLiteType> TYPE_MAP = new HashMap<Class<?>, SQLiteType>() {
		{
			put(byte.class, SQLiteType.INTEGER);
			put(short.class, SQLiteType.INTEGER);
			put(int.class, SQLiteType.INTEGER);
			put(long.class, SQLiteType.INTEGER);
			put(boolean.class, SQLiteType.INTEGER);
			put(Byte.class, SQLiteType.INTEGER);
			put(Short.class, SQLiteType.INTEGER);
			put(Integer.class, SQLiteType.INTEGER);
			put(Long.class, SQLiteType.INTEGER);
			put(Boolean.class, SQLiteType.INTEGER);
			put(float.class, SQLiteType.REAL);
			put(double.class, SQLiteType.REAL);
			put(Float.class, SQLiteType.REAL);
			put(Double.class, SQLiteType.REAL);
			put(char.class, SQLiteType.TEXT);
			put(Character.class, SQLiteType.TEXT);
			put(String.class, SQLiteType.TEXT);
			put(byte[].class, SQLiteType.BLOB);
			put(Byte[].class, SQLiteType.BLOB);
		}
	};
	
	// Database creation
	public static String createTableDefinition(TableInfo tableInfo) {
		final ArrayList<String> definitions = new ArrayList<String>();

		for (Field field : tableInfo.getFields()) {
			String definition = createColumnDefinition(tableInfo, field);
			if (!TextUtils.isEmpty(definition)) {
				definitions.add(definition);
			}
		}

		return String.format("CREATE TABLE IF NOT EXISTS %s (%s);", tableInfo.getTableName(),
				TextUtils.join(", ", definitions));
	}
	
	@SuppressWarnings("unchecked")
	public static String createColumnDefinition(TableInfo tableInfo, Field field) {
		StringBuilder definition = new StringBuilder();

		Class<?> type = field.getType();
		final String name = tableInfo.getColumnName(field);
		final Column column = field.getAnnotation(Column.class);

		if (TYPE_MAP.containsKey(type)) {
			definition.append(name);
			definition.append(" ");
			definition.append(TYPE_MAP.get(type).toString());
		}

		if (!TextUtils.isEmpty(definition)) {
			if (column.length() > -1) {
				definition.append("(");
				definition.append(column.length());
				definition.append(")");
			}

			if (name.equals("Id")) {
				definition.append(" PRIMARY KEY AUTOINCREMENT");
			}
			
			if (column.notNull()) {
				definition.append(" NOT NULL");
			}
		}
		else {
			Log.i(TAG,"No type mapping for: " + type.toString());
		}

		return definition.toString();
	}
	
	public static void execSql(String sql) {
		DBManager.openDatabase().execSQL(sql);
	}
	
	public static <T extends DBDomain> T rawQuerySingle(Class<? extends DBDomain> type, String sql, String[] selectionArgs) {
		List<T> entities = rawQuery(type, sql, selectionArgs);

		if (entities.size() > 0) {
			return entities.get(0);
		}

		return null;
	}
	
	public static <T extends DBDomain> List<T> rawQuery(Class<? extends DBDomain> type, String sql, String[] selectionArgs) {
		Cursor cursor = DBManager.openDatabase().rawQuery(sql, selectionArgs);
		List<T> entities = parseCursor(type, cursor);
		cursor.close();

		return entities;
	}

	private static <T extends DBDomain> List<T> parseCursor(Class<? extends DBDomain> type,
			Cursor cursor) {
		final List<T> entities = new ArrayList<T>();

		try {
			Constructor<?> entityConstructor = type.getConstructor();

			if (cursor.moveToFirst()) {
				do {
					DBDomain entity = DBManager.getEntity(type, cursor.getLong(cursor.getColumnIndex("Id")));
					
					if(entity == null) {
						entity = (T) entityConstructor.newInstance();
					}

					entity.loadFromCursor(cursor);
					entities.add((T) entity);
				}
				while (cursor.moveToNext());
			}

		}
		catch (Exception e) {
		}

		return entities;
	}
}
