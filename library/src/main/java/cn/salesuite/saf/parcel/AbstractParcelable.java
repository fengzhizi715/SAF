/**
 * 
 */
package cn.salesuite.saf.parcel;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import cn.salesuite.saf.log.L;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 抽象的基类Parcelable，可以直接继承该类
 * @author Tony Shen
 *
 */
public class AbstractParcelable implements Parcelable {

	public AbstractParcelable() {
	}

	public AbstractParcelable(Parcel in) {
		String className = in.readString();
		L.i("Constructor: "
				+ this.getClass().getSimpleName() + "; In parcel: " + className);
		try {
			read(this, in);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static final Parcelable.Creator<AbstractParcelable> CREATOR = new Parcelable.Creator<AbstractParcelable>() {
		public AbstractParcelable createFromParcel(Parcel in) {
			// get class from first parcelled item
			Class<?> parceledClass;
			try {
				parceledClass = Class.forName(in.readString());
				L.i("Creator: " + parceledClass.getSimpleName());
				// create instance of that class
				AbstractParcelable model = (AbstractParcelable) parceledClass
						.newInstance();
				read(model, in);
				return model;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		}

		public AbstractParcelable[] newArray(int size) {
			return new AbstractParcelable[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getClass().getName());
		try {
			write(this, dest);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	protected static void write(AbstractParcelable model, Parcel out)
			throws IllegalArgumentException, IllegalAccessException {
		L.i("dehydrating... "
				+ model.getClass().toString());
		Field[] fields = model.getClass().getDeclaredFields();
		Arrays.sort(fields, compareMemberByName);
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getType().equals(int.class)) {
				out.writeInt(field.getInt(model));
			} else if (field.getType().equals(double.class)) {
				out.writeDouble(field.getDouble(model));
			} else if (field.getType().equals(float.class)) {
				out.writeFloat(field.getFloat(model));
			} else if (field.getType().equals(long.class)) {
				out.writeLong(field.getLong(model));
			} else if (field.getType().equals(String.class)) {
				out.writeString((String) field.get(model));
			} else if (field.getType().equals(boolean.class)) {
				out.writeByte(field.getBoolean(model) ? (byte) 1 : (byte) 0);
			} else if (field.getType().equals(Date.class)) {
				Date date = (Date) field.get(model);
				if (date != null) {
					out.writeLong(date.getTime());
				} else {
					out.writeLong(0);
				}
			} else if (AbstractParcelable.class.isAssignableFrom(field.getType())) {
				L.e("AbstractParcelable" + " ("
						+ field.getType().toString() + ")");
				out.writeParcelable((AbstractParcelable) field.get(model), 0);
			} else {
				L.e("Could not write field to parcel: "
						+ " (" + field.getType().toString() + ")");
			}
		}
	}

	protected static void read(AbstractParcelable model, Parcel in)
			throws IllegalArgumentException, IllegalAccessException {
		L.i("rehydrating... "
				+ model.getClass().toString());
		Field[] fields = model.getClass().getDeclaredFields();
		Arrays.sort(fields, compareMemberByName);

		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getType().equals(int.class)) {
				field.set(model, in.readInt());
			} else if (field.getType().equals(double.class)) {
				field.set(model, in.readDouble());
			} else if (field.getType().equals(float.class)) {
				field.set(model, in.readFloat());
			} else if (field.getType().equals(long.class)) {
				field.set(model, in.readLong());
			} else if (field.getType().equals(String.class)) {
				field.set(model, in.readString());
			} else if (field.getType().equals(boolean.class)) {
				field.set(model, in.readByte() == 1);
			} else if (field.getType().equals(Date.class)) {
				Date date = new Date(in.readLong());
				field.set(model, date);
			} else if (AbstractParcelable.class.isAssignableFrom(field.getType())) {
				L.e("read AbstractParcelable: " + " ("
						+ field.getType().toString() + ")");
				field.set(model,
						in.readParcelable(field.getType().getClassLoader()));
			} else {
				L.e("Could not read field from parcel: "
						+ field.getName() + " (" + field.getType().toString()
						+ ")");
			}
		}
	}

	/**
	 * Comparator object for Members, Fields, and Methods
	 */
	private static Comparator<Field> compareMemberByName = new CompareMemberByName();

	private static class CompareMemberByName implements Comparator {
		public int compare(Object o1, Object o2) {
			String s1 = ((Member) o1).getName();
			String s2 = ((Member) o2).getName();

			if (o1 instanceof Method) {
				s1 += getSignature((Method) o1);
				s2 += getSignature((Method) o2);
			} else if (o1 instanceof Constructor) {
				s1 += getSignature((Constructor) o1);
				s2 += getSignature((Constructor) o2);
			}
			return s1.compareTo(s2);
		}
	}

	/**
	 * 计算该类的jvm签名
	 */
	private static String getSignature(Class clazz) {
		String type = null;
		if (clazz.isArray()) {
			Class cl = clazz;
			int dimensions = 0;
			while (cl.isArray()) {
				dimensions++;
				cl = cl.getComponentType();
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < dimensions; i++) {
				sb.append("[");
			}
			sb.append(getSignature(cl));
			type = sb.toString();
		} else if (clazz.isPrimitive()) {
			if (clazz == Integer.TYPE) {
				type = "I";
			} else if (clazz == Byte.TYPE) {
				type = "B";
			} else if (clazz == Long.TYPE) {
				type = "J";
			} else if (clazz == Float.TYPE) {
				type = "F";
			} else if (clazz == Double.TYPE) {
				type = "D";
			} else if (clazz == Short.TYPE) {
				type = "S";
			} else if (clazz == Character.TYPE) {
				type = "C";
			} else if (clazz == Boolean.TYPE) {
				type = "Z";
			} else if (clazz == Void.TYPE) {
				type = "V";
			}
		} else {
			type = "L" + clazz.getName().replace('.', '/') + ";";
		}
		return type;
	}

	private static String getSignature(Method method) {
		StringBuilder sb = new StringBuilder();

		sb.append("(");

		Class[] params = method.getParameterTypes(); // avoid clone
		for (int j = 0; j < params.length; j++) {
			sb.append(getSignature(params[j]));
		}
		sb.append(")");
		sb.append(getSignature(method.getReturnType()));
		return sb.toString();
	}

	private static String getSignature(Constructor cons) {
		StringBuilder sb = new StringBuilder();

		sb.append("(");

		Class[] params = cons.getParameterTypes(); // avoid clone
		for (int j = 0; j < params.length; j++) {
			sb.append(getSignature(params[j]));
		}
		sb.append(")V");
		return sb.toString();
	}
}
