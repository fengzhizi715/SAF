/**
 * 
 */
package cn.salesuite.saf.log;

import android.util.Log;

import com.safframework.tony.common.utils.Preconditions;
import com.safframework.tony.common.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 日志包装类
 * @author Tony Shen
 *
 */
public class L {
	
	public enum LogLevel {
		ERROR {
			public int getValue() {
				return 0;
			}
		},
		WARN {
			public int getValue() {
				return 1;
			}
		},
		INFO {
			public int getValue() {
				return 2;
			}
		},
		DEBUG {
			public int getValue() {
				return 3;
			}
		};

		public abstract int getValue();
	}
	
	private static String TAG = "SAF_L";
	
	public static LogLevel logLevel = LogLevel.DEBUG; // 日志的等级，可以进行配置，最好在Application中进行全局的配置
	
	public static void init(Class<?> clazz) {
		TAG = clazz.getSimpleName();
	}

	/**
	 * 支持用户自己传tag，可扩展性更好
	 * @param tag
     */
	public static void init(String tag) {
		TAG = tag;
	}
	
	public static void e(String msg) {
		if (LogLevel.ERROR.getValue() <= logLevel.getValue()) {
			
			if(Preconditions.isNotBlank(msg)) {

				String s = getMethodNames();
				Log.e(TAG, String.format(s,msg));
			}
		}
	}
	
	public static void e(String msg,Throwable tr) {
		if (LogLevel.ERROR.getValue() <= logLevel.getValue()) {
			
			if(Preconditions.isNotBlank(msg)) {
				Log.e(TAG, msg,tr);
			}
		}
	}
	
	public static void w(String msg) {
		if (LogLevel.WARN.getValue() <= logLevel.getValue()) {
			
			if(Preconditions.isNotBlank(msg)) {

				String s = getMethodNames();
				Log.w(TAG, String.format(s,msg));
			}
		}
	}
	
	public static void w(String msg,Throwable tr) {
		if (LogLevel.WARN.getValue() <= logLevel.getValue()) {
			
			if(Preconditions.isNotBlank(msg)) {
				Log.w(TAG, msg,tr);
			}
		}
	}
	
	public static void i(String msg) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(Preconditions.isNotBlank(msg)) {

				String s = getMethodNames();
				Log.i(TAG, String.format(s,msg));
			}
		}
	}
	
	/**
	 * 
	 * @param tag 自定义tag
	 * @param msg
	 */
	public static void i(String tag,String msg) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(Preconditions.isNotBlanks(tag,msg)) {

				String s = getMethodNames();
				Log.i(tag, String.format(s,msg));
			}
		}
	}
	
	public static void i(String msg,Throwable tr) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(Preconditions.isNotBlank(msg)) {
				Log.i(TAG, msg,tr);
			}
		}
	}
	
	public static void d(String msg) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(Preconditions.isNotBlank(msg)) {

				String s = getMethodNames();
				Log.d(TAG, String.format(s,msg));
			}
		}
	}
	
	/**
	 * 
	 * @param tag 自定义tag
	 * @param msg
	 */
	public static void d(String tag,String msg) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(Preconditions.isNotBlank(msg)) {

				String s = getMethodNames();
				Log.d(tag, String.format(s,msg));
			}
		}
	}
	
	public static void d(String msg,Throwable tr) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(Preconditions.isNotBlank(msg)) {
				Log.d(TAG, msg,tr);
			}
		}
	}

	public static void json(Map map) {
		if(map!=null) {

			try {
				JSONObject jsonObject = new JSONObject(map);
				String message = jsonObject.toString(LoggerPrinter.JSON_INDENT);
				message = message.replaceAll("\n","\n║ ");
				String s = getMethodNames();
				System.out.println(String.format(s,message));
			} catch (JSONException e) {
				e("Invalid Json");
			}
		}
	}

	public static void json(Object object) {

		if (object == null) {
			d("object is null");
			return;
		}

		try {
			String objStr = StringUtils.printObject(object);
			JSONObject jsonObject = new JSONObject(objStr);
			String message = jsonObject.toString(LoggerPrinter.JSON_INDENT);
			message = message.replaceAll("\n","\n║ ");
			String s = getMethodNames();
			System.out.println(String.format(s,message));
		} catch (JSONException e) {
			e("Invalid Json");
		}
	}

	public static void json(String json) {

		if (Preconditions.isBlank(json)) {
			d("Empty/Null json content");
			return;
		}

		try {
			json = json.trim();
			if (json.startsWith("{")) {
				JSONObject jsonObject = new JSONObject(json);
				String message = jsonObject.toString(LoggerPrinter.JSON_INDENT);
				message = message.replaceAll("\n","\n║ ");
				String s = getMethodNames();
				System.out.println(String.format(s,message));
				return;
			}
			if (json.startsWith("[")) {
				JSONArray jsonArray = new JSONArray(json);
				String message = jsonArray.toString(LoggerPrinter.JSON_INDENT);
				message = message.replaceAll("\n","\n║ ");
				String s = getMethodNames();
				System.out.println(String.format(s,message));
				return;
			}
			e("Invalid Json");
		} catch (JSONException e) {
			e("Invalid Json");
		}
	}

	private static String getMethodNames() {
		StackTraceElement[] sElements = Thread.currentThread().getStackTrace();

		int stackOffset = LoggerPrinter.getStackOffset(sElements);

		stackOffset++;
		StringBuilder builder = new StringBuilder();
		builder.append(LoggerPrinter.TOP_BORDER).append("\r\n")
				// 添加当前线程名
				.append("║ " + "Thread: " + Thread.currentThread().getName()).append("\r\n")
				.append(LoggerPrinter.MIDDLE_BORDER).append("\r\n")
				// 添加类名、方法名、行数
				.append("║ ")
				.append(sElements[stackOffset].getClassName())
				.append(".")
				.append(sElements[stackOffset].getMethodName())
				.append(" ")
				.append(" (")
				.append(sElements[stackOffset].getFileName())
				.append(":")
				.append(sElements[stackOffset].getLineNumber())
				.append(")")
				.append("\r\n")
				.append(LoggerPrinter.MIDDLE_BORDER).append("\r\n")
				// 添加打印的日志信息
				.append("║ ").append("%s").append("\r\n")
				.append(LoggerPrinter.BOTTOM_BORDER).append("\r\n");
		return builder.toString();
	}
}
