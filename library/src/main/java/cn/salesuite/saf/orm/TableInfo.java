/**
 * 
 */
package cn.salesuite.saf.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.salesuite.saf.orm.annotation.Column;
import cn.salesuite.saf.orm.annotation.Table;

/**
 * @author Tony Shen
 *
 */
public final class TableInfo {

	private Class<? extends DBDomain> mType;
	private String mTableName;

	private Map<Field, String> mColumnNames = new HashMap<Field, String>();
	
	public TableInfo(Class<? extends DBDomain> type) {
		mType = type;

		final Table tableAnnotation = type.getAnnotation(Table.class);
		if (tableAnnotation != null) {
			mTableName = tableAnnotation.name();
		}
		else {
			mTableName = type.getSimpleName();
		}

		List<Field> fields = new ArrayList<Field>(Arrays.asList(type.getDeclaredFields()));
		fields.add(getIdField(type));

		for (Field field : fields) {
			if (field.isAnnotationPresent(Column.class)) {
				final Column columnAnnotation = field.getAnnotation(Column.class);
				mColumnNames.put(field, columnAnnotation.name());
			}
		}
	}
	
	public Class<? extends DBDomain> getType() {
		return mType;
	}

	public String getTableName() {
		return mTableName;
	}

	public Collection<Field> getFields() {
		return mColumnNames.keySet();
	}

	public String getColumnName(Field field) {
		return mColumnNames.get(field);
	}
	
	private Field getIdField(Class<?> type) {
		if (type.equals(DBDomain.class)) {
			try {
				return type.getDeclaredField("mId");
			}
			catch (NoSuchFieldException e) {
			}
		}
		else if (type.getSuperclass() != null) {
			return getIdField(type.getSuperclass());
		}

		return null;
	}

}
