/**
 * 
 */
package cn.salesuite.saf.log;

import cn.salesuite.saf.app.SAFActivity;
import cn.salesuite.saf.app.SAFFragment;
import cn.salesuite.saf.app.SAFFragmentActivity;
import cn.salesuite.saf.utils.SAFUtil;
import cn.salesuite.saf.utils.StringHelper;
import android.util.Log;

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
	
	public L(SAFActivity activity) {
		TAG = activity.TAG;
	}
	
	public L(SAFFragmentActivity activity) {
		TAG = activity.TAG;
	}
	
	public L(SAFFragment fragment) {
		TAG = fragment.TAG;
	}
	
	public static void e(String msg) {
		if (LogLevel.ERROR.getValue() <= logLevel.getValue()) {
			
			if(StringHelper.isNotBlank(msg)) {
				Log.e(TAG, msg);
			}
		}
	}
	
	public static void e(String msg,Object ...args) {
		if (LogLevel.ERROR.getValue() <= logLevel.getValue()) {
			
			if(StringHelper.isNotBlank(msg)) {
				Log.e(TAG, String.format(msg, args));
			}
		}
	}
	
	public static void e(String msg,Throwable tr) {
		if (LogLevel.ERROR.getValue() <= logLevel.getValue()) {
			
			if(StringHelper.isNotBlank(msg)) {
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
				Log.e(TAG, SAFUtil.printObject(object));
			}
		}
	}
	
	public static void w(String msg) {
		if (LogLevel.WARN.getValue() <= logLevel.getValue()) {
			
			if(StringHelper.isNotBlank(msg)) {
				Log.w(TAG, msg);
			}
		}
	}
	
	public static void w(String msg,Object ...args) {
		if (LogLevel.WARN.getValue() <= logLevel.getValue()) {
			
			if(StringHelper.isNotBlank(msg)) {
				Log.w(TAG, String.format(msg, args));
			}
		}
	}
	
	public static void w(String msg,Throwable tr) {
		if (LogLevel.WARN.getValue() <= logLevel.getValue()) {
			
			if(StringHelper.isNotBlank(msg)) {
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
				Log.w(TAG, SAFUtil.printObject(object));
			}
		}
	}
	
	public static void i(String msg) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(StringHelper.isNotBlank(msg)) {
				Log.i(TAG, msg);
			}
		}
	}
	
	public static void i(String msg,Object ...args) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(StringHelper.isNotBlank(msg)) {
				Log.i(TAG, String.format(msg, args));
			}
		}
	}
	
	public static void i(String msg,Throwable tr) {
		if (LogLevel.INFO.getValue() <= logLevel.getValue()) {
			
			if(StringHelper.isNotBlank(msg)) {
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
				Log.i(TAG, SAFUtil.printObject(object));
			}
		}
	}
	
	public static void d(String msg) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(StringHelper.isNotBlank(msg)) {
				Log.d(TAG, msg);
			}
		}
	}
	
	public static void d(String msg,Object ...args) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(StringHelper.isNotBlank(msg)) {
				Log.d(TAG, String.format(msg, args));
			}
		}
	}
	
	public static void d(String msg,Throwable tr) {
		if (LogLevel.DEBUG.getValue() <= logLevel.getValue()) {
			
			if(StringHelper.isNotBlank(msg)) {
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
				Log.d(TAG, SAFUtil.printObject(object));
			}
		}
	}
}
