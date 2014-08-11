/**
 * 
 */
package cn.salesuite.saf.orm;

import java.lang.reflect.Field;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.salesuite.saf.orm.annotation.Column;

/**
 * 
 * @author Tony Shen
 *
 */
public class DBDomain {

	@Column(name = "Id")
	private Long mId = null;
	
	private TableInfo mTableInfo;
	
	public Long getId() {
		return mId;
	}
	
	public DBDomain() {
		mTableInfo = DBManager.getTableInfo(getClass());
	}
	
	public int count() {
		SQLiteDatabase db = DBManager.openDatabase();
        Cursor cursor  = db.rawQuery("select count(*) from "+mTableInfo.getTableName(),null);
        int count = 0;
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        return count;
	}
	
	public void save() {
		final SQLiteDatabase db = DBManager.openDatabase();
		final ContentValues values = new ContentValues();

		for (Field field : mTableInfo.getFields()) {
			final String fieldName = mTableInfo.getColumnName(field);
			Class<?> fieldType = field.getType();

			field.setAccessible(true);

			try {
				Object value = field.get(this);

				if (value == null) {
					values.putNull(fieldName);
				}
				else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
					values.put(fieldName, (Byte) value);
				}
				else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
					values.put(fieldName, (Short) value);
				}
				else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
					values.put(fieldName, (Integer) value);
				}
				else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
					values.put(fieldName, (Long) value);
				}
				else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
					values.put(fieldName, (Float) value);
				}
				else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
					values.put(fieldName, (Double) value);
				}
				else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
					values.put(fieldName, (Boolean) value);
				}
				else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
					values.put(fieldName, value.toString());
				}
				else if (fieldType.equals(String.class)) {
					values.put(fieldName, value.toString());
				}
				else if (fieldType.equals(Byte[].class) || fieldType.equals(byte[].class)) {
					values.put(fieldName, (byte[]) value);
				}
			}
			catch (IllegalArgumentException e) {
			}
			catch (IllegalAccessException e) {
			}
		}

		if (mId == null) {
			mId = db.insert(mTableInfo.getTableName(), null, values);
		} else {
			db.update(mTableInfo.getTableName(), values, "Id=" + mId, null);
		}
	}
	
	public void delete() {
		SQLiteDatabase db = DBManager.openDatabase();
		if (mId!=null) {
			db.delete(mTableInfo.getTableName(), "Id=" + mId, null);
		}
	}
	
	public void delete(int id) {
		SQLiteDatabase db = DBManager.openDatabase();
		db.delete(mTableInfo.getTableName(), "Id=" + id, null);
	}
	
	public <T extends DBDomain>T get(int id) {
		return SQLiteUtils.rawQuerySingle(getClass(),"select * from "+mTableInfo.getTableName()+" where Id="+id,null);
	}
	
	public <T extends DBDomain> List<T> getAll() {
		return executeQuery("select * from "+mTableInfo.getTableName());
	}
	
	public <T extends DBDomain> List<T> executeQuery(String query) {
		return SQLiteUtils.rawQuery(getClass() , query ,null);
	}
	
	/**
	 * 支持类似于new Autocomplete().executeQuery("select * from autocomplete where KEY_WORDS = ? and Id = ?","testtest","1");的查询</br>
	 * 表示查询select * from autocomplete where KEY_WORDS = 'testtest' and Id = '1'
	 * @param query
	 * @param args
	 * @return
	 */
	public <T extends DBDomain> List<T> executeQuery(String query,String...args) {
		int length = args.length;
		if (length==0) {
			return executeQuery(query);
		}
		
		for(int i=0;i<length;i++) {
			query = query.replaceFirst("\\?", "'"+args[i]+"'");
		}
		return SQLiteUtils.rawQuery(getClass() , query ,null);
	}
	
	public final void loadFromCursor(Cursor cursor) {
		for (Field field : mTableInfo.getFields()) {
			final String fieldName = mTableInfo.getColumnName(field);
			Class<?> fieldType = field.getType();
			final int columnIndex = cursor.getColumnIndex(fieldName);

			if (columnIndex < 0) {
				continue;
			}

			field.setAccessible(true);

			try {
				boolean columnIsNull = cursor.isNull(columnIndex);

				Object value = null;

				if (columnIsNull) {
					field = null;
				}
				else if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
					value = cursor.getInt(columnIndex);
				}
				else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
					value = cursor.getInt(columnIndex);
				}
				else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
					value = cursor.getInt(columnIndex);
				}
				else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
					value = cursor.getLong(columnIndex);
				}
				else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
					value = cursor.getFloat(columnIndex);
				}
				else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
					value = cursor.getDouble(columnIndex);
				}
				else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
					value = cursor.getInt(columnIndex) != 0;
				}
				else if (fieldType.equals(Character.class) || fieldType.equals(char.class)) {
					value = cursor.getString(columnIndex).charAt(0);
				}
				else if (fieldType.equals(String.class)) {
					value = cursor.getString(columnIndex);
				}
				else if (fieldType.equals(Byte[].class) || fieldType.equals(byte[].class)) {
					value = cursor.getBlob(columnIndex);
				}

				// Set the field value
				if (value != null) {
					field.set(this, value);
				}
			}
			catch (IllegalArgumentException e) {
			}
			catch (IllegalAccessException e) {
			}
			catch (SecurityException e) {
			}
		}

		if (mId != null) {
			DBManager.addEntity(this);
		}
	}
	
	@Override
	public String toString() {
		return mTableInfo.getTableName() + "@" + getId();
	}

	@Override
	public boolean equals(Object obj) {
		final DBDomain other = (DBDomain) obj;

		return this.mId != null && (this.mTableInfo.getTableName().equals(other.mTableInfo.getTableName()))
				&& (this.mId.equals(other.mId));
	}
}
