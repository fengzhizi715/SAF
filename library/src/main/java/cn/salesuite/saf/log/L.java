/**
 * 
 */
package cn.salesuite.saf.log;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.salesuite.saf.app.SAFActivity;
import cn.salesuite.saf.app.SAFFragment;
import cn.salesuite.saf.app.SAFFragmentActivity;
import cn.salesuite.saf.utils.SAFUtils;
import cn.salesuite.saf.utils.StringUtils;

/**
 * 日志包装类
 * @author Tony Shen
 *
 */
public class L {
	
	public static enum LogLevel {
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
	};
	
	private static String TAG = "SAF_L";
	
	public static LogLevel logLevel = LogLevel.DEBUG; // 日志的等级，可以进行配置
	
	public static void init (SAFActivity activity) {
		TAG = activity.TAG;
	}
	
	public static void init(SAFFragmentActivity activity) {
		TAG = activity.TAG;
	}
	
	public static void init(SAFFragment fragment) {
		TAG = fragment.TAG;
	}
	
	public static void init(Class<?> clazz) {
		TAG = clazz.getSimpleName();
	}
	
	public static void e(String msg) {
		if (LogLevel.ERROR.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.e(TAG, msg);
			}
		}
	}
	
	public static void e(String msg,Object ...args) {
		if (LogLevel.ERROR.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.e(TAG, String.format(msg, args));
			}
		}
	}
	
	public static void e(String msg,Throwable tr) {
		if (LogLevel.ERROR.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.e(TAG, msg,tr);
			}
		}
	}
	
	/**
	 * 打印普通java对象
	 * @param object
	 */
	public static void e(Object object) {
		if (LogLevel.ERROR.getValue() <= logLevel.getValue()) {
			
			if(object!=null) {
				Log.e(TAG, SAFUtils.printObject(object));
			}
		}
	}
	
	public static void w(String msg) {
		if (LogLevel.WARN.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.w(TAG, msg);
			}
		}
	}
	
	public static void w(String msg,Object ...args) {
		if (LogLevel.WARN.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.w(TAG, String.format(msg, args));
			}
		}
	}
	
	public static void w(String msg,Throwable tr) {
		if (LogLevel.WARN.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.w(TAG, msg,tr);
			}
		}
	}
	
	/**
	 * 打印普通java对象
	 * @param object
	 */
	public static void w(Object object) {
		if (LogLevel.WARN.getValue() <= logLevel.getValue()) {
			
			if(object!=null) {
				Log.w(TAG, SAFUtils.printObject(object));
			}
		}
	}
	
	public static void i(String msg) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {

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
	public static void iWithTag(String tag,String msg) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.i(tag, msg);
			}
		}
	}
	
	/**
	 * 
	 * @param prefix 前缀，用于区分是哪个要打印的日志
	 * @param msg
	 */
	public static void i(String prefix,String  msg) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.i(TAG, prefix+"="+msg);
			}
		}
	}
	
	public static void i(String msg,Object ...args) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.i(TAG, String.format(msg, args));
			}
		}
	}
	
	public static void i(String msg,Throwable tr) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.i(TAG, msg,tr);
			}
		}
	}
	
	/**
	 * 打印普通java对象
	 * @param object
	 */
	public static void i(Object object) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(object!=null) {
				String s = getMethodNames();
				Log.i(TAG, String.format(s,SAFUtils.printObject(object)));
			}
		}
	}
	
	/**
	 * 
	 * @param tag 自定义tag
	 * @param object
	 */
	public static void iWithTag(String tag,Object object) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(object!=null) {
				Log.i(tag, SAFUtils.printObject(object));
			}
		}
	}
	
	/**
	 * 打印普通java对象
	 * @param object
	 */
	public static void i(String prefix,Object object) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(object!=null) {
				Log.i(TAG, prefix+"="+SAFUtils.printObject(object));
			}
		}
	}
	
	public static void d(String msg) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.d(TAG, msg);
			}
		}
	}
	
	/**
	 * 
	 * @param tag 自定义tag
	 * @param msg
	 */
	public static void dWithTag(String tag,String msg) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.d(tag, msg);
			}
		}
	}
	
	/**
	 * 
	 * @param prefix 前缀，用于区分是哪个要打印的日志
	 * @param msg
	 */
	public static void d(String prefix,String  msg) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.d(TAG, prefix+"="+msg);
			}
		}
	}
	
	public static void d(String msg,Object ...args) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.d(TAG, String.format(msg, args));
			}
		}
	}
	
	public static void d(String msg,Throwable tr) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(StringUtils.isNotBlank(msg)) {
				Log.d(TAG, msg,tr);
			}
		}
	}
	
	/**
	 * 打印普通java对象
	 * @param object
	 */
	public static void d(Object object) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(object!=null) {
				Log.d(TAG, SAFUtils.printObject(object));
			}
		}
	}
	
	/**
	 * 
	 * @param tag 自定义tag
	 * @param object
	 */
	public static void dWithTag(String tag,Object object) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(object!=null) {
				Log.d(tag, SAFUtils.printObject(object));
			}
		}
	}
	
	/**
	 * 打印普通java对象
	 * @param object
	 */
	public static void d(String prefix,Object object) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(object!=null) {
				Log.d(TAG, prefix+"="+SAFUtils.printObject(object));
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
